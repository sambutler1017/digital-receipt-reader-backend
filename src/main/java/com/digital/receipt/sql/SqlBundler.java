package com.digital.receipt.sql;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.digital.receipt.common.enums.QueryTag;
import com.digital.receipt.common.exceptions.MaliciousSqlQueryException;
import com.digital.receipt.sql.domain.SqlParams;

import org.springframework.stereotype.Service;

/**
 * Sql Bundler for converting and reading sql files with their associted params
 * and field conditions.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Service
public class SqlBundler {

    private boolean hasWhereCondition;

    private boolean deleteNextLine;

    private long previousSpaceCount;

    private ScriptEngineManager sem;

    /**
     * Default constructor to intialize the class level variables. These will be
     * used when doing logical test with the sql conditions.
     * 
     * @see #replaceCondition(String)
     */
    public SqlBundler() {
        hasWhereCondition = false;
        deleteNextLine = false;
        previousSpaceCount = 1L;
        sem = new ScriptEngineManager();
    }

    /**
     * Bundles the query string and params together. Any instances of the params
     * found in the query will be replaced with it's corresponding values. If a
     * value is null and is contained in a condition, this condition will be
     * omitted.
     * 
     * @param query  The query string to be populated with the params.
     * @param params The params to populate the string with.
     * @return {@link String} of the modified query.
     */
    public String bundle(List<String> query, SqlParams params) {
        resetConditionStatus();

        int index = 0;
        for (String line : query) {
            if (deleteNextLine && line.indexOf(line.trim()) > previousSpaceCount) {
                query.set(index, "");
            } else {
                deleteNextLine = false;

                if (getConditionTagMatcher(query.get(index)).find()) {
                    query.set(index, replaceCondition(line, params));
                } else if (getParamTagMatcher(query.get(index)).find()) {
                    query.set(index, replaceParam(line, params));
                }
                previousSpaceCount = line.indexOf(line.trim());
            }
            index++;
        }

        return validateQuery(query.stream().collect(Collectors.joining(" ")).replaceAll("\\s{2,}", " ").trim());
    }

    /**
     * Method that will check if the string that was formed caontains an malicious
     * characters that shouldn't be in the query. If it does it will throw a
     * {@link MaliciousSqlQueryException}
     * 
     * @param q The query to be validated.
     * @return {@link String} of the query if it is valid.
     */
    public String validateQuery(String q) throws MaliciousSqlQueryException {
        if (q.contains(";") || q.contains("--") || q.contains("\\")) {
            throw new MaliciousSqlQueryException();
        } else {
            return q;
        }
    }

    /**
     * Gets the matcher for checking if the given line has a condition tag in it.
     * 
     * @param line The line to get the matcher for
     * @return {@link Matcher} of the regex
     */
    private Matcher getConditionTagMatcher(String line) {
        String conditionList = Arrays.asList(QueryTag.values()).stream().map(v -> v.toString())
                .collect(Collectors.joining("|"));
        return Pattern.compile(String.format("@(%s)", conditionList)).matcher(line);
    }

    /**
     * Gets the matcher for checking if the given line has a param tag in the
     * string.
     * 
     * @param line The line to get the matcher for
     * @return {@link Matcher} of the regex
     */
    private Matcher getParamTagMatcher(String line) {
        return Pattern.compile(":[\\w]+:").matcher(line);
    }

    /**
     * Replaces the annotation if the condition in the params field does not
     * evaulate to true.
     * 
     * @param line   The sql line being studied
     * @param params The params to use to check expression.
     * @return {@link String} of the completed line.
     */
    private String replaceCondition(String line, SqlParams sqlParams) {
        Matcher m = getConditionTagMatcher(line);

        if (m.find()) {
            QueryTag condition = QueryTag.getEnumFromString(m.group(0).trim());
            Matcher ex = Pattern.compile("(?<=\\()(.*?)(?=\\))").matcher(line);
            Matcher exParams = getParamTagMatcher(line);

            ex.find();
            String exp = ex.group(0).trim();
            boolean valid = false;

            if (isExpression(line)) {
                valid = modifyExpressionCondition(line, sqlParams, exp, exParams);
            } else {
                valid = modifySingleCondition(line, sqlParams, exParams);
            }

            String replacingValue = condition.text();
            if (valid) {
                if (condition.equals(QueryTag.IF)) {
                    replacingValue = "";
                } else if (!hasWhereCondition) {
                    hasWhereCondition = true;
                    replacingValue = QueryTag.WHERE.text();
                }
            } else {
                deleteNextLine = true;
                replacingValue = "";
            }
            return line.replace(String.format("%s(%s)", condition.annotation(), exp), replacingValue);
        }
        return line;
    }

    /**
     * Replace the param found in the string with it's corresponding value.
     * 
     * @param line   The line that values will be replaced in.
     * @param params The params to map to the string.
     * @return {@link String} with all the values replaced.
     */
    private String replaceParam(String line, SqlParams params) {
        Matcher m = getParamTagMatcher(line);

        while (m.find()) {
            String paramField = m.group(0).replace(":", "").trim();
            Object paramValue = params.getValue(paramField);

            line = paramValue instanceof Collection
                    ? getReplacedCollectionParam(line, paramField, castCollection(paramValue))
                    : getReplacedSingleParam(line, paramField, paramValue);
        }
        return line;
    }

    /**
     * This will determine if the given expression in the annotation parentheses is
     * valid and the expression evaultes to true.
     * 
     * @param line      The string in the query being studied.
     * @param sqlParams The params to be inserted into the string.
     * @param exp       The expression to replace the values in.
     * @param expParams The params to insert into the expression.
     * @return {@link boolean} determining if the expression results to true.
     */
    private boolean modifyExpressionCondition(String line, SqlParams sqlParams, String exp, Matcher expParams) {
        String expressionString = exp;
        while (expParams.find()) {
            String expressionName = expParams.group(0).trim();
            expressionString = expressionString.replace(expressionName,
                    buildExpressionParam(sqlParams.getValue(expressionName.replace(":", ""))));
        }
        return evalutateExpression(expressionString);
    }

    /**
     * Builds out the js script to determine if the string expression is valid.
     * 
     * @param value The value to add to the expression.
     * @return {@link String} with the complete expression.
     */
    private String buildExpressionParam(Object value) {
        String conditionValue = "";
        if (value != null) {
            conditionValue = value.toString();
        }
        return String.format("('%s' != null && '%s' != false)", conditionValue, conditionValue);
    }

    /**
     * This is called if only a single param value is in the parentheses and
     * determines if the value exist.
     * 
     * @param line      The string in the query being studied.
     * @param sqlParams The params to be inserted into the string.
     * @param expParams The params to insert into the expression.
     * @return {@link boolean} determining if the expression results to true.
     */
    private boolean modifySingleCondition(String line, SqlParams sqlParams, Matcher expParams) {
        expParams.find();
        return sqlParams.getValue(expParams.group(0).replace(":", "").trim()) != null;
    }

    /**
     * Determines if the string being studied is an expression or single param
     * value.
     * 
     * @param line The string to detect
     * @return {@link boolean} determining if it is an expression or not.
     */
    private boolean isExpression(String line) {
        return Pattern.compile("[\\|\\|\\&\\&\\!]+").matcher(line).find();
    }

    /**
     * Determines the result of the expression. If the expression can't be found or
     * evaulated then it will return false. Otherwise it will evaualte the
     * expression.
     * 
     * @param expression The expression to be evaulated.
     * @return {@link boolean} of the expressions status.
     */
    private boolean evalutateExpression(String expression) {
        try {

            ScriptEngine se = sem.getEngineByName("JavaScript");
            return Boolean.parseBoolean(se.eval(expression).toString());

        } catch (ScriptException e) {
            // Comes in here when it is an invalid expression
            return false;
        }
    }

    /**
     * Replace a param tag with a list of values.
     * 
     * @param line       The line that needs to be replaced with the params.
     * @param field      The field we are looking for in the string.
     * @param listValues The values to represent as a string.
     * @return {@link String} representation of the list.
     */
    private String getReplacedCollectionParam(String line, String field, Collection<Object> listValues) {
        if (line.contains("LIKE")) {
            String paramLikeList = listValues.stream().map(v -> String.format("LIKE '%%%s%%'", v.toString()))
                    .collect(Collectors.joining("OR"));
            return line.replace(String.format("LIKE :%s:", field), paramLikeList);
        } else {
            String paramList = listValues.stream().map(v -> String.format("'%s'", v.toString()))
                    .collect(Collectors.joining(","));
            return line.replace(String.format("= :%s:", field),
                    String.format("%s", String.format("IN (%s)", paramList)));
        }
    }

    /**
     * This will replace a single param in the line.
     * 
     * @param line  The line that needs to be replaced with the params.
     * @param field The field we are looking for in the string.
     * @param value The value we are trying to add to the string.
     * @return {@link String} of the line with the replaces param.
     */
    private String getReplacedSingleParam(String line, String field, Object value) {
        return line.replace(String.format(":%s:", field), String.format("'%s'", value.toString()));
    }

    /**
     * Resets the variables to their original values. This is so if a back to back
     * request is called with the same object then it will not use the previously
     * set values.
     * 
     * @see #bundle(List, SqlParams)
     */
    private void resetConditionStatus() {
        hasWhereCondition = false;
        deleteNextLine = false;
    }

    /**
     * Will case the object to a list of objects.
     * 
     * @param <T> The type the object values should be cast too.
     * @param obj What object needs to be a list.
     * @return {@link List} of objects.
     */
    @SuppressWarnings("unchecked")
    public <T extends Collection<?>> T castCollection(Object obj) {
        return (T) obj;
    }
}
