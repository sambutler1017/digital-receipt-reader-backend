package com.digital.receipt.sql.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Sql parameter to be used when building out a sql file.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
public class SqlParams {

    private final Map<String, Object> values = new LinkedHashMap<>();

    /**
     * Empty constructor to be used and add values too view {@code addValue}
     * 
     * @see #addValue(String, Object)
     */
    public SqlParams() {
    }

    /**
     * Create a new MapSqlParameterSource, with one value comprised of the supplied
     * arguments.
     * 
     * @param paramName the name of the parameter
     * @param value     the value of the parameter
     * @see #addValue(String, Object)
     */
    public SqlParams(String paramName, @Nullable Object value) {
        addValue(paramName, value);
    }

    /**
     * Add a parameter to this parameter source.
     * 
     * @param paramName the name of the parameter
     * @param value     the value of the parameter
     * @return a reference to this parameter source, so it's possible to chain
     *         several calls together
     */
    public SqlParams addValue(String paramName, @Nullable Object value) {
        Assert.notNull(paramName, "Parameter name must not be null");
        this.values.put(paramName, value);
        return this;
    }

    /**
     * Expose the current parameter values as read-only Map.
     */
    public Map<String, Object> getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    /**
     * Checks to see if the following param name exists in the map. If it does then
     * it will return true, otherwise false.
     * 
     * @param paramName The name of the param to search for in the map.
     * @return {@link boolean} based on the contains result
     */
    public boolean hasValue(String paramName) {
        return this.values.containsKey(paramName);
    }

    /**
     * Gets the object based on the given param name.
     * 
     * @param paramName The name of the param to search for in the map.
     * @return Returns an {@link Object} of the value in the map, which can be null.
     * @throws IllegalArgumentException If the paramName does not exist in the map.
     */
    @Nullable
    public Object getValue(String paramName) {
        if (!hasValue(paramName)) {
            return null;
        }
        return this.values.get(paramName);
    }
}
