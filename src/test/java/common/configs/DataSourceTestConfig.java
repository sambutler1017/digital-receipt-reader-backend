package common.configs;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.digital.receipt.service.activeProfile.ActiveProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Configuration class for running test with datasource.
 * 
 * @author Sam Butler
 * @since August 28, 2021
 */
@Configuration
@Profile("test")
public class DataSourceTestConfig extends DatabaseTestProfile {

    @Autowired
    public DataSourceTestConfig() {
        super(new ActiveProfile());
    }

    /**
     * Create the datasouce config.
     * 
     * @return {@link DataSource} with the set fields.
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource() throws SQLException {
        DriverManagerDataSource ds = new DriverManagerDataSource();
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
     * @throws SQLException
     */
    @Bean
    public JdbcTemplate jdbcTemplate() throws SQLException {
        return new JdbcTemplate(dataSource());
    }

}
