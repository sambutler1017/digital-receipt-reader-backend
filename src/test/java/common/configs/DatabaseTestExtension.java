package common.configs;

import java.io.File;

import com.digital.receipt.service.util.PasswordUtil;
import com.digital.receipt.sql.SqlClient;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DatabaseTestExtension implements BeforeAllCallback, AfterAllCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseTestExtension.class);
    private static long testClassCount = 0;
    private static boolean started = false;

    private DatabaseTestProfile dbProfile;

    public DatabaseTestExtension() {
        this.dbProfile = new DatabaseTestProfile();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;
            generateTestSchema();
            testClassCount = countFilesInDirectory(new File("src\\test\\java\\com\\digital\\receipt"));
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        testClassCount--;
        if (testClassCount == 0) {
            LOGGER.info(String.format("Cleaning up database and dropping schema '%s'.",
                    System.getProperty("TEST_SCHEMA_NAME")));
            new SqlClient(getDefaultDataSource())
                    .execute(String.format("DROP SCHEMA `%s`", System.getProperty("TEST_SCHEMA_NAME")));
        }
    }

    /**
     * Get the default datasource to create the test schema.
     * 
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource getDefaultDataSource() {
        return generateDataSource(String.format(dbProfile.DB_URL, "receipt_db"), dbProfile.getUsername(),
                dbProfile.getPassword());
    }

    /**
     * Get the default datasource to create the test schema.
     * 
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource getTestDataSource() {
        return generateDataSource(String.format(dbProfile.DB_URL, System.getProperty("TEST_SCHEMA_NAME")),
                dbProfile.getUsername(), dbProfile.getPassword());
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
     * @throws Exception
     */
    private String generateTestSchema() throws Exception {
        String testSchema = String.format("receipt_test_%d", PasswordUtil.generatePasswordSalt());
        SqlClient sqlClient = new SqlClient(getDefaultDataSource());

        sqlClient.execute(String.format("CREATE SCHEMA `%s`", testSchema));
        System.setProperty("TEST_SCHEMA_NAME", testSchema);
        new TestFactory(new SqlClient(getTestDataSource())).initSchema();

        return testSchema;
    }

    /**
     * Count files in a directory (including files in all subdirectories)
     * 
     * @param directory the directory to start in
     * @return the total number of files
     */
    private int countFilesInDirectory(File directory) {
        LOGGER.info("Path: " + directory.getPath());
        LOGGER.info("File Exists: " + directory.exists());
        LOGGER.info("User DIR: " + System.getProperty("user.dir"));

        int count = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                count++;
            }
            if (file.isDirectory()) {
                count += countFilesInDirectory(file);
            }
        }
        return count;
    }
}