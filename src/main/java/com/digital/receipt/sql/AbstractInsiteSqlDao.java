package com.digital.receipt.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import com.digital.receipt.common.enums.InsiteSqlTags;
import com.digital.receipt.common.exceptions.SqlFragmentNotFoundException;
import com.digital.receipt.service.activeProfile.ActiveProfile;
import com.digital.receipt.sql.domain.InsiteSqlParams;

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
public abstract class AbstractInsiteSqlDao {

    private final String defaultSqlPath = "%s/resources/dao/%s.insitesql";

    @Autowired
    private ActiveProfile activeProfile;

    private boolean startOfFragmentFound;
    private boolean endOfFragmentFound;

    /**
     * Default constructor to set the inital values for the start of fragment and
     * end of fragment booleans.
     * 
     * @see #isContainedInFragment(String, String)
     */
    public AbstractInsiteSqlDao() {
        startOfFragmentFound = false;
        endOfFragmentFound = false;
    }

    /**
     * Gets the sql based on the given fragment name.
     * 
     * @param fragmentName Name of the sql fragment to search for.
     * @return {@link AbstractInsiteSqlDao} instance containing the sql string.
     * @throws SqlFragmentNotFoundException If the fragment can not be found in the
     *                                      given file.
     * @throws IOException
     */
    public String getSql(String fragmentName) throws SqlFragmentNotFoundException, IOException {
        String filePath = String.format(defaultSqlPath, activeProfile.getEnvironmentUrl(), getChildClassName());
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String result = br.lines().filter(s -> isContainedInFragment(s, fragmentName))
                .collect(Collectors.joining("\n"));
        br.close();
        return result;
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
        if (startOfFragmentFound && !endOfFragmentFound) {
            return true;
        }

        if (value.contains(String.format("%s(%s)", InsiteSqlTags.NAME.toString(), fragmentName))) {
            startOfFragmentFound = true;
        } else if (startOfFragmentFound && !endOfFragmentFound && value.contains(InsiteSqlTags.NAME.toString())) {
            endOfFragmentFound = true;
        }

        return false;
    }
}
