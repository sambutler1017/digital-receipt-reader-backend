package com.digital.receipt.tests.configs;

import javax.sql.DataSource;

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
public class DataSourceTestConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(String.format("jdbc:mysql://databasePI.ddnsfree.com/%s?%s", "receipt_db", getDBParams()));
        ds.setUsername(getUsername());
        ds.setPassword(getPassword());
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    private String getUsername() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_USERNAME")
                : "sambutler1017asd";
    }

    private String getPassword() {
        return System.getProperty("APP_ENVIRONMENT") != null ? System.getProperty("MYSQL_PASSWORD") : "password12";
    }

    private String getDBParams() {
        return "useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }
}
