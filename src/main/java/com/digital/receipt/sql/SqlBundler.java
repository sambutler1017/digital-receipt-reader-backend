package com.digital.receipt.sql;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.digital.receipt.common.enums.QueryTag;
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

    private String replaceCondition(String line, SqlParams params) {
        Matcher m = getConditionTagMatcher(line);

        if (m.find()) {
            QueryTag condition = QueryTag.getEnumFromString(m.group(0).trim());
            Matcher paramCondition = Pattern.compile(String.format("(?<=%s\\(:)(\\w+)(?=:\\))", condition.annotation()))
                    .matcher(line);

            paramCondition.find();
            String paramField = paramCondition.group(0).trim();

            String replacingValue = condition.text();
            if (params.getValue(paramField) != null && !condition.equals(QueryTag.IF)) {
                if (!hasWhereCondition) {
                    hasWhereCondition = true;
                    replacingValue = QueryTag.WHERE.text();
                }
            } else {
                deleteNextLine = true;
                replacingValue = "";
            }
            return getReplacedConditionLine(condition, replacingValue, line, paramField);
        }
        return line;
    }

    private String getReplacedConditionLine(QueryTag condition, String replacingValue, String line, String paramField) {
        return line.replace(String.format("%s(:%s:)", condition.annotation(), paramField), replacingValue);
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
                deleteNextLine = false;
                return "";
            }

            if (deleteNextLine) {
                deleteNextLine = line.chars().filter(c -> c == (int) '\t').count() >= 2;
                return "";
            }

            return paramValue instanceof Collection
                    ? getReplacedCollectionParam(line, paramField, castCollection(paramValue))
                    : getReplacedSingleParam(line, paramField, paramValue);
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
    private String getReplacedCollectionParam(String line, String field, Collection<Object> listValues) {
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
    public <T extends Collection<?>> T castCollection(Object obj) {
        return (T) obj;
    }
}
