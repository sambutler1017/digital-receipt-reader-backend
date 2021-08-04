package com.digital.receipt.sql;

import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
import com.digital.receipt.sql.domain.InsiteSqlParams;

import org.springframework.stereotype.Service;

/**
 * Abstract class for building the DAO classes and running queries against the
 * database
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Service
public abstract class AbstractInsiteSqlDao {

    private String queryFile;

    /**
     * Default constructor to set the file that should be looked in for the
     * fragment.
     * 
     * @param fileName The name of the file to search in.
     */
    public AbstractInsiteSqlDao(String fileName) {
        queryFile = fileName;
    }

    /**
     * Gets the sql based on the given fragment name.
     * 
     * @param fragmentName Name of the sql fragment to search for.
     * @return {@link AbstractInsiteSqlDao} instance containing the sql string.
     * @throws SqlFragmentNotFoundException If the fragment can not be found in the
     *                                      given file.
     */
    public String getSql(String fragmentName) throws SqlFragmentNotFoundException {
        return "It works";
    }

    /**
     * Gets a {@link InsiteSqlParams} object to store the params to be used on the
     * sql query.
     * 
     * @param name  The name of the field to store the object under.
     * @param value The object to store in the params.
     * @return Instance of {@link InsiteSqlParams} object with the added param.
     */
    public InsiteSqlParams params(String name, Object value) {
        return new InsiteSqlParams(name, value);
    }
}
