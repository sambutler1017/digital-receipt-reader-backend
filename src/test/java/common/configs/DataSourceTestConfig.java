package common.configs;

import javax.sql.DataSource;

import com.digital.receipt.service.util.PasswordUtil;
import com.digital.receipt.sql.SqlClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import common.factory.TestFactory;

/**
 * Configuration class for running test with datasource.
 * 
 * @author Sam Butler
 * @since August 28, 2021
 */
@Configuration
@ComponentScan(basePackages = "com.digital.receipt")
@Profile("test")
public class DataSourceTestConfig extends DatabaseTestProfile {

    /**
     * Create the datasouce config.
     * 
     * @return {@link DataSource} with the set fields.
     * @throws Exception
     */
    @Bean
    public DataSource dataSource() throws Exception {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        generateTestSchema();

        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(String.format(DB_URL, System.getProperty("TEST_SCHEMA_NAME")));
        ds.setUsername(getUsername());
        ds.setPassword(getPassword());
        return ds;
    }

    /**
     * Config for the jdbcTemplate off of the datasource.
     * 
     * @return {@link JdbcTemplate} with the configured datasource.
     * @throws Exception
     */
    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Get the default datasource to create the test schema.
     * 
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource getDefaultDataSource() {
        return generateDataSource(String.format(DB_URL, "receipt_db"), getUsername(), getPassword());
    }

    /**
     * Get the default datasource to create the test schema.
     * 
     * @return {@link DriverManagerDataSource} source of the db.
     */
    private DriverManagerDataSource getTestDataSource() {
        return generateDataSource(String.format(DB_URL, System.getProperty("TEST_SCHEMA_NAME")), getUsername(),
                getPassword());
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

}
