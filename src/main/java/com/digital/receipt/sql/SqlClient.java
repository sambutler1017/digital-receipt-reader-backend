package com.digital.receipt.sql;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

/**
 * Common SQL Service to make API calls to the database
 * 
 * @author Sam Butler
 * @since August 1, 2021
 */
@Service
public class SqlClient {

	private static JdbcTemplate jdbcTemplateObject;

	Properties prop = new Properties();

	/**
	 * Constructor to autowire the datasource with the template object so it can be
	 * used to make database calls
	 * 
	 * @see {@link DataSource}
	 */
	@Autowired
	public SqlClient(DataSource source) {
		jdbcTemplateObject = new JdbcTemplate(source);
	}

	/**
	 * Gets a a page of rows from the given query
	 * 
	 * @param <T>    - Class object to return as
	 * @param query  - Query to execute
	 * @param mapper - The mapper to manipulate the data as
	 * @return Generic object
	 */
	public <T> List<T> getPage(String query, RowMapper<T> mapper) {
		return jdbcTemplateObject.query(query, mapper);
	}

	/**
	 * Gets a single row from the given query
	 * 
	 * @param <T>    - Class object to return as
	 * @param query  - Query to execute
	 * @param mapper - The mapper to manipulate the data as
	 * @return Generic object
	 */
	public <T> T getTemplate(String query, RowMapper<T> mapper) {
		return jdbcTemplateObject.queryForObject(query, mapper);
	}

	/**
	 * Gets list of maps
	 * 
	 * @param query - Query to execute
	 * @return Generic object
	 */
	public List<Map<String, Object>> getListMap(String query) {
		return jdbcTemplateObject.queryForList(query);
	}

	/**
	 * Execute the delete query
	 * 
	 * @param query - Query to be executed
	 */
	public void delete(String query) {
		jdbcTemplateObject.execute(query);
	}

	/**
	 * Update method for a query.
	 * 
	 * @param query - The insert query to be run
	 * @return Integer value of the auto_increment id if there is one
	 */
	public Optional<Integer> update(String query) {
		return post(query);
	}

	/**
	 * Common post method to be used when doing inserts into the database
	 * 
	 * @param query - The insert query to be run
	 * @return Integer value of the auto_increment id if there is one
	 */
	public Optional<Integer> post(String query) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplateObject.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			return ps;
		}, keyHolder);
		try {
			return Optional.of(keyHolder.getKey().intValue());
		} catch (Exception e) {
			return Optional.of(-1);
		}
	}
}
