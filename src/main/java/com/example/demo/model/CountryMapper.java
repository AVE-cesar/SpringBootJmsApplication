package com.example.demo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CountryMapper implements RowMapper<Country> {

	public Country mapRow(ResultSet resultSet, int i) throws SQLException {

		Country country = new Country();
		country.setId(resultSet.getLong("id"));
		country.setName(resultSet.getString("name"));

		return country;
	}
}
