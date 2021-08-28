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
@Profile("tmep")
public class DataSourceTestConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(
                "jdbc:mysql://databasePI.ddnsfree.com/receipt_db?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        ds.setUsername(System.getProperty("MYSQL_USERNAME"));
        ds.setPassword(System.getProperty("MYSQL_PASSWORD"));
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
