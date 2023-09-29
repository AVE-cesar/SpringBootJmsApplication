package com.example.demo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.Country;
import com.example.demo.model.CountryMapper;

@Component
public class CountryDaoImpl implements CountryDAO {

	private final static Logger logger = LoggerFactory.getLogger(CountryDaoImpl.class);

	// @Autowired
	JdbcTemplate jdbcTemplate;

	private final String SQL_FIND_PERSON = "select * from countries where id = ?";
	private final String SQL_DELETE_PERSON = "delete from countries where id = ?";
	private final String SQL_UPDATE_PERSON = "update countries set name = ? where id = ?";
	private final String SQL_GET_ALL = "select * from countries";
	private final String SQL_INSERT_PERSON = "insert into countries(id, name) values(?,?)";

	@Autowired
	public CountryDaoImpl(@Lazy DataSource h2DataSource_1) {
		jdbcTemplate = new JdbcTemplate(h2DataSource_1);
	}

	@Override
	public Country getCountryById(Long id) {
		logger.info("getCountryById {}", id);
		return jdbcTemplate.queryForObject(SQL_FIND_PERSON, new Object[] { id }, new CountryMapper());
	}

	@Override
	public List<Country> getAllCountries() {
		return jdbcTemplate.query(SQL_GET_ALL, new CountryMapper());
	}

	@Override
	public boolean deleteCountry(Country country) {
		logger.info("on supprime le pays {}", country.getId());
		return jdbcTemplate.update(SQL_DELETE_PERSON, country.getId()) > 0;
	}

	@Override
	public boolean updateCountry(Country country) {
		return jdbcTemplate.update(SQL_UPDATE_PERSON, country.getName(), country.getId()) > 0;
	}

	@Override
	public boolean createCountry(Country country) {
		return jdbcTemplate.update(SQL_INSERT_PERSON, country.getId(), country.getName()) > 0;
	}

}
