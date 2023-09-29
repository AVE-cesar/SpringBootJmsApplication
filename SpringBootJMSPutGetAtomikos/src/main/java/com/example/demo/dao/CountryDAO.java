package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Country;

public interface CountryDAO {

	Country getCountryById(Long id);

	List<Country> getAllCountries();

	boolean deleteCountry(Country country);

	boolean updateCountry(Country country);

	boolean createCountry(Country country);

}
