package com.digital.receipt.common.abstracts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.digital.receipt.common.enums.SqlTag;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
import com.digital.receipt.service.activeProfile.ActiveProfile;
import com.digital.receipt.sql.SqlClient;
import com.digital.receipt.sql.domain.SqlParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Abstract class for building the DAO classes and running queries against the
 * database
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Service
public abstract class AbstractSqlDao {

    private final String defaultSqlPath = "%s/resources/dao/%s.sql";

    private boolean startOfFragmentFound;

    private boolean endOfFragmentFound;

    @Autowired
    protected ActiveProfile activeProfile;

    @Autowired
    protected SqlClient sqlClient;

    /**
     * Gets the sql based on the given fragment name.
     * 
     * @param fragmentName Name of the sql fragment to search for.
     * @return {@link AbstractSqlDao} instance containing the sql string.
     * @throws SqlFragmentNotFoundException If the fragment can not be found in the
     *                                      given file.
     * @throws IOException
     */
    public List<String> getSql(String fragmentName) throws Exception {
        resetFragmentStatus();

        String filePath = String.format(defaultSqlPath, activeProfile.getEnvironmentUrl(), getChildClassName());
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        List<String> result = br.lines().filter(s -> isContainedInFragment(s, fragmentName))
                .collect(Collectors.toList());
        br.close();
        return result;
    }

    /**
     * Gets a {@link SqlParams} object to store the params to be used on the sql
     * query.
     * 
     * @param name  The name of the field to store the object under.
     * @param value The object to store in the params.
     * @return Instance of {@link SqlParams} object with the added param.
     */
    public SqlParams params(String name, Object value) {
        return new SqlParams(name, value);
    }

    /**
     * This will get the name of the child class. This is used when finding what
     * file the sql fragment is contained in.
     * 
     * @return {@link String} of the filename
     * @see #getSql(String)
     */
    private String getChildClassName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Determines if the provided string in the file should be included in the
     * fragment.
     * 
     * @param value        The string value to decide if it is needed.
     * @param fragmentName The fragement section we are looking for.
     * @return {@link boolean} to say if the string should be kept on the stream.
     * @see #getSql(String)
     */
    private boolean isContainedInFragment(String value, String fragmentName) {
        if (startOfFragmentFound && !endOfFragmentFound && value.contains(SqlTag.NAME.toString())) {
            endOfFragmentFound = true;
        }

        if (value.contains(String.format("%s(%s)", SqlTag.NAME.toString(), fragmentName))) {
            startOfFragmentFound = true;
        } else if (startOfFragmentFound && !endOfFragmentFound) {
            return true;
        }

        return false;
    }

    /**
     * Resets the variables to their original values. This is so if a back to back
     * request is called with the same object then it will not use the previously
     * set values.
     * 
     * @see #getSql(String)
     */
    private void resetFragmentStatus() {
        startOfFragmentFound = false;
        endOfFragmentFound = false;
    }
}
