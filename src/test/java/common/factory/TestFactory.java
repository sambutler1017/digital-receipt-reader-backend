package common.factory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.digital.receipt.common.abstracts.AbstractSqlDao;
import com.digital.receipt.service.activeProfile.ActiveProfile;
import com.digital.receipt.sql.SqlClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Test Factory class for dealing with building of the test database and
 * cleanup.
 * 
 * @author Sam Butler
 * @since August 28, 2021
 */
@Service
public class TestFactory extends AbstractSqlDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestFactory.class);

    private SqlClient sqlClient;

    @Autowired
    public TestFactory(SqlClient sqlClient) {
        super(new ActiveProfile().getTestEnvironmentUrl());
        this.sqlClient = sqlClient;
    }

    /**
     * Initailizes the schema including the tables and contraints needed.
     * 
     * @throws Exception If the the fragment can't be found.
     */
    public void initSchema() throws Exception {
        initTables();
        initContraints();
        LOGGER.info(String.format("Initialization of Schema '%s' Complete!", System.getProperty("TEST_SCHEMA_NAME")));
    }

    /**
     * Creates the default tables needed for the schema.
     * 
     * @throws Exception If the the fragment can't be found.
     */
    private void initTables() throws Exception {
        LOGGER.info(String.format("Creating tables on schema: %s", System.getProperty("TEST_SCHEMA_NAME")));
        sqlClient.batch(getQueryList("setupTables"));
    }

    /**
     * Creates the contraints needed for the tables.
     * 
     * @throws Exception If the the fragment can't be found.
     */
    private void initContraints() throws Exception {
        LOGGER.info("Adding table Contraints ...");
        sqlClient.batch(getQueryList("setupContraints"));
    }

    /**
     * Get the query array with the removed null and empty string values from the
     * list.
     * 
     * @param fragment Name of the query section to search for.
     * @return {@link String[]} of the queries to run.
     * @throws Exception If the fragment can't be found.
     */
    private String[] getQueryList(String fragment) throws Exception {
        List<String> queries = Arrays.asList(getSql(fragment).stream().collect(Collectors.joining("")).split(";"));
        queries.removeAll(Arrays.asList("", " ", null));
        return queries.toArray(new String[0]);
    }
}
