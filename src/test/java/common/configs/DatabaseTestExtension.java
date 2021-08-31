package common.configs;

import com.digital.receipt.sql.SqlClient;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

/**
 * Extension class for initializing data for the database.
 * 
 * @author Sam Butler
 * @since August 28, 2021
 */
@Service
public class DatabaseTestExtension implements AfterAllCallback {
    private static boolean afterAllInitialized = false;
    private DatabaseTestProfile dbProfile;

    public DatabaseTestExtension() {
        this.dbProfile = new DatabaseTestProfile();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (afterAllInitialized) {
            new SqlClient(getDefaultDataSource())
                    .execute(String.format("DROP SCHEMA `%s`", System.getProperty("TEST_SCHEMA_NAME")));
        } else {
            afterAllInitialized = true;
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
}