package com.digital.receipt.sql;

import com.digital.receipt.sql.domain.InsiteSqlParams;

import org.springframework.stereotype.Service;

/**
 * Sql Bundler for converting and reading sql files with their associted params
 * and field conditions.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Service
public class InsiteSqlBundle {

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
    public String bundle(String query, InsiteSqlParams params) {
        return "";
    }
}
