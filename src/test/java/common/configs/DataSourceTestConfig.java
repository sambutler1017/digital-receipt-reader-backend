package common.configs;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
}
