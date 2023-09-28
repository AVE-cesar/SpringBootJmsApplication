package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.FileUtils;
import com.example.demo.dao.CountryDAO;
import com.example.demo.model.Country;

@Component
public class BusinessServiceImpl implements BusinessService {

	private final static Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

	@Autowired
	CountryDAO countryDAO;

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	AsyncJMSPut asyncJMSPut;

	@Override
	@Transactional
	public void doLogic(String... args) throws Exception {
		logger.info("--------------________________________------------");

		logger.info("our logic starts here");

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("fileTest.txt");
		String data;
		try {
			data = FileUtils.readFromInputStream(inputStream);
			logger.info(data);
		} catch (IOException e) {
			logger.warn("", e);
		}

		if (countryDAO != null) {

			Country andorre = new Country(1000L, "Andorre");

			Country luxembourg = new Country(2000L, "Luxembourg");

			logger.info("résultat de la suppression: {}", countryDAO.deleteCountry(andorre));
			logger.info("résultat de la suppression: {}", countryDAO.deleteCountry(luxembourg));

			Country countryFound = countryDAO.getCountryById(1L);

			logger.info("le pays doit être les USA: {}", countryFound.getName());

			countryFound = null;

			boolean created = countryDAO.createCountry(andorre);

			countryFound = countryDAO.getCountryById(andorre.getId());
			logger.info("le pays Andorre ={} doit être créé: {}", andorre.getName(), countryFound.getId());

			// Send a list of JMS messages
			// Send a JMS message
			logger.info("Sending a JMS message.");
			for (int i = 0; i < 1000; i++) {
				asyncJMSPut.sendJMS(i);
			}

			if ("SimulateAnError".equalsIgnoreCase(args[0])) {
				throw new Exception("SimulateAnError and rollback ALL");
			}
			created = countryDAO.createCountry(luxembourg);

			countryFound = countryDAO.getCountryById(luxembourg.getId());
			logger.info("le pays Luxembourg ={} doit être créé: {}", luxembourg.getName(), countryFound.getId());

		} else {
			logger.warn("on ne veut pas en être là");
		}

	}

}
