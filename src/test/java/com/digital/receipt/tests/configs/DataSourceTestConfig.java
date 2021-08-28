package com.digital.receipt.tests.configs;

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
@Profile("unknownTest")
public class DataSourceTestConfig {

    @Autowired
    private ActiveProfile activeProfile;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(String.format(
                "jdbc:mysql://databasePI.ddnsfree.com/%s?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                generateTestSchema()));
        ds.setUsername(activeProfile.isLocalEnvironment() ? "" : System.getenv("MYSQL_USERNAME"));
        ds.setPassword(activeProfile.isLocalEnvironment() ? "" : System.getenv("MYSQL_PASSWORD"));
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Generates a test schema so the DAO test can run on a actual database data
     * besides production.
     * 
     * @return {@link String of the database name}
     */
    private String generateTestSchema() {
        return "TestSchemaName";
    }
}
