package com.digital.receipt.sql;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import com.digital.receipt.sql.domain.SqlParams;

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

	@Autowired
	private SqlBundler bundler;

	private static JdbcTemplate jdbcTemplateObject;

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
	 * @param <T>    - Class object to return as.
	 * @param query  - Query to execute.
	 * @param params the params to attach to the query string.
	 * @param mapper - The mapper to manipulate the data as.
	 * @return Generic object
	 */
	public <T> List<T> getPage(List<String> query, SqlParams params, RowMapper<T> mapper) {
		return jdbcTemplateObject.query(bundler.bundle(query, params), mapper);
	}

	/**
	 * Gets a single row from the given query
	 * 
	 * @param <T>    Class object to return as.
	 * @param query  Query to execute.
	 * @param params the params to attach to the query string.
	 * @param mapper The mapper to manipulate the data as.
	 * @return Generic object
	 */
	public <T> T getTemplate(List<String> query, SqlParams params, RowMapper<T> mapper) {
		return jdbcTemplateObject.queryForObject(bundler.bundle(query, params), mapper);
	}

	/**
	 * Gets list of maps.
	 * 
	 * @param query  The query to be executed.
	 * @param params The params to add to the query.
	 * @return Generic object
	 */
	public List<Map<String, Object>> getListMap(List<String> query, SqlParams params) {
		return jdbcTemplateObject.queryForList(bundler.bundle(query, params));
	}

	/**
	 * Execute the delete query.
	 * 
	 * @param query  The query to be executed.
	 * @param params The params to add to the query.
	 */
	public void delete(List<String> query, SqlParams params) {
		jdbcTemplateObject.execute(bundler.bundle(query, params));
	}

	/**
	 * Update method for a query.
	 * 
	 * @param query  The query to be executed.
	 * @param params The params to add to the query.
	 * @return Integer value of the auto_increment id if there is one
	 */
	public Optional<Integer> update(List<String> query, SqlParams params) {
		return post(query, params);
	}

	/**
	 * Common post method to be used when doing inserts into the database.
	 * 
	 * @param query  The query to be executed.
	 * @param params The params to add to the query.
	 * @return Integer value of the auto_increment id if there is one.
	 */
	public Optional<Integer> post(List<String> query, SqlParams params) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplateObject.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(bundler.bundle(query, params),
					Statement.RETURN_GENERATED_KEYS);
			return ps;
		}, keyHolder);
		try {
			return Optional.of(keyHolder.getKey().intValue());
		} catch (Exception e) {
			return Optional.of(-1);
		}
	}

	/**
	 * Execute the given query string as it is.
	 * 
	 * @param query The query to be executed.
	 */
	public void execute(String query) {
		jdbcTemplateObject.execute(query);
	}

	/**
	 * Execute multiple queries at once.
	 * 
	 * @param query The query to be executed.
	 */
	public void batch(String... querys) {
		jdbcTemplateObject.batchUpdate(querys);
	}
}
