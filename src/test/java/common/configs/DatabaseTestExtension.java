package common.configs;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

import com.digital.receipt.service.activeProfile.ActiveProfile;
import com.digital.receipt.sql.SqlClient;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import common.factory.TestFactory;

/**
 * Extension class for initializing data for the database.
 * 
 * @author Sam Butler
 * @since August 28, 2021
 */
@Service
public class DatabaseTestExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;
    private DatabaseTestProfile dbProfile;

    private DriverManagerDataSource defaultDatasource;
    private SqlClient sqlClient;
    private TestFactory testFactory;

    public DatabaseTestExtension() {
        this.dbProfile = new DatabaseTestProfile(new ActiveProfile());
        this.defaultDatasource = getDefaultDataSource();
        this.sqlClient = new SqlClient(defaultDatasource);
        this.testFactory = new TestFactory(this.sqlClient);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;
            context.getRoot().getStore(GLOBAL).put("DB_EXTENSION", this);
            generateTestSchema();
            schemaInit();
        }
    }

    @Override
    public void close() {
        sqlClient.execute(String.format("DROP SCHEMA `%s`", System.getProperty("TEST_SCHEMA_NAME")));
    }

    /**
     * Get the default datasource to create the test schema.
     * 
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource getDefaultDataSource() {
        return generateDataSource(
                "jdbc:mysql://databasePI.ddnsfree.com/receipt_db?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                dbProfile.getUsername(), dbProfile.getPassword());
    }

    /**
     * Get the default datasource to create the test schema.
     * 
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource getTestDataSource() {
        String url = "jdbc:mysql://databasePI.ddnsfree.com/%s?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        return generateDataSource(String.format(url, System.getProperty("TEST_SCHEMA_NAME")), dbProfile.getUsername(),
                dbProfile.getPassword());
    }

    /**
     * Method to generate a datasource object.
     * 
     * @param url      The url to hit the database.
     * @param username Username to access with.
     * @param password Password to access with.
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource generateDataSource(String url, String username, String password) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    /**
     * Generate the test Schema name and store it.
     * 
     * @return {@link String} of the test schema name.
     */
    private String generateTestSchema() {
        String testSchema = String.format("receipt_test_%d",
                (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

        sqlClient.execute(String.format("CREATE SCHEMA `%s`", testSchema));
        System.setProperty("TEST_SCHEMA_NAME", testSchema);
        return testSchema;
    }

    /**
     * Init the schema with the test datasource.
     * 
     * @throws Exception If the sql client can be initialized.
     */
    private void schemaInit() throws Exception {
        testFactory = new TestFactory(new SqlClient(getTestDataSource()));
        testFactory.initSchema();
    }
}