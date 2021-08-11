package com.digital.receipt.sql;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.digital.receipt.common.enums.QueryStatement;
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

    /**
     * Default constructor to intialize the class level variables. These will be
     * used when doing logical test with the sql conditions.
     * 
     * @see #replaceCondition(String)
     */
    public SqlBundler() {
        hasWhereCondition = false;
        deleteNextLine = false;
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
            if (getConditionTagMatcher(line).find()) {
                query.set(index, replaceCondition(line, params));
            } else if (getParamTagMatcher(line).find()) {
                query.set(index, replaceParam(line, params));
            }
            index++;
        }

        return query.stream().collect(Collectors.joining(" ")).replaceAll("\\s{2,}", " ").trim();
    }

    /**
     * Gets the matcher for checking if the given line has a condition tag in it.
     * 
     * @param line The line to get the matcher for
     * @return {@link Matcher} of the regex
     */
    private Matcher getConditionTagMatcher(String line) {
        String conditionList = Arrays.asList(QueryStatement.values()).stream().map(v -> v.toString())
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
        return Pattern.compile(":([\\w]+):").matcher(line);
    }

    private String replaceCondition(String line, SqlParams params) {
        Matcher m = getConditionTagMatcher(line);

        if (m.find()) {
            QueryStatement condition = QueryStatement.getEnumFromString(m.group(0).trim());
            Matcher paramCondition = Pattern.compile(String.format("(?<=%s\\(:)(\\w+)(?=:\\))", condition.annotation()))
                    .matcher(line);

            paramCondition.find();
            String paramField = paramCondition.group(0).trim();
            if (params.getValue(paramField) != null) {
                if (condition.equals(QueryStatement.IF)) {
                    return hasIfCondition(line, paramField);
                } else {
                    QueryStatement replacingCondition = condition;
                    if (!hasWhereCondition) {
                        replacingCondition = QueryStatement.WHERE;
                        hasWhereCondition = true;
                    }
                    return line.replace(String.format("%s(:%s:)", condition.annotation(), paramField),
                            replacingCondition.text());
                }
            } else {
                deleteNextLine = true;
                return line.replace(String.format("%s(:%s:)", condition.annotation(), paramField), "");
            }
        }
        return line;
    }

    /**
     * Get rid of the IF statement and keep the logic inside.
     * 
     * @param line       The query line to be updated
     * @param paramField The field that we are looking for in the map.
     * @return {@link String} of the updated line.
     */
    public String hasIfCondition(String line, String paramField) {
        return line.replace(String.format("%s(:%s:)", QueryStatement.IF.annotation(), paramField), "");
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

            if (paramValue == null) {
                return "";
            }

            if (deleteNextLine) {
                long tabCount = line.chars().filter(c -> c == (int) '\t').count();
                if (tabCount >= 2) {
                    return "";
                } else {
                    deleteNextLine = false;
                    return "";
                }
            }

            if (paramValue instanceof List)
                return getReplacedListParam(line, paramField, castList(paramValue));
            else if (paramValue instanceof Set)
                return getReplacedSetParam(line, paramField, castSet(paramValue));
            else
                return getReplacedSingleParam(line, paramField, paramValue);
        }
        return line;
    }

    /**
     * Replace a param tag with a list of values.
     * 
     * @param line       The line that needs to be replaced with the params.
     * @param field      The field we are looking for in the string.
     * @param listValues The values to represent as a string.
     * @return {@link String} representation of the list.
     */
    private String getReplacedListParam(String line, String field, List<Object> listValues) {
        String paramList = listValues.stream().map(v -> String.format("'%s'", v.toString()))
                .collect(Collectors.joining(","));
        return line.replace(String.format("= :%s:", field), String.format("%s", String.format("IN (%s)", paramList)));
    }

    /**
     * Replace a param tag with a set of values.
     * 
     * @param line       The line that needs to be replaced with the params.
     * @param field      The field we are looking for in the string.
     * @param listValues The values to represent as a string.
     * @return {@link String} representation of the list.
     */
    private String getReplacedSetParam(String line, String field, Set<Object> listValues) {
        String paramList = listValues.stream().map(v -> String.format("'%s'", v.toString()))
                .collect(Collectors.joining(","));
        return line.replace(String.format("= :%s:", field), String.format("%s", String.format("IN (%s)", paramList)));
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
    public <T extends List<?>> T castList(Object obj) {
        return (T) obj;
    }

    /**
     * Will case the object to a set of objects.
     * 
     * @param <T> The type the object values should be cast too.
     * @param obj What object needs to be a list.
     * @return {@link Set} of objects.
     */
    @SuppressWarnings("unchecked")
    public <T extends Set<?>> T castSet(Object obj) {
        return (T) obj;
    }
}
