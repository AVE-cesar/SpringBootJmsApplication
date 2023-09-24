package com.example.demo;

import java.io.InputStream;
import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.example.demo.dao.CountryDAO;
import com.example.demo.model.Country;
import com.example.demo.service.BusinessService;

@SpringBootApplication
@EnableJms
public class DemoApplication implements CommandLineRunner {

	private final static Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	BusinessService businessService;
	
	/**
	 * Démarrage de l'application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Starting");

		SpringApplication.run(DemoApplication.class, args);

		logger.debug("Let's inspect the arguments received from the command line:");
		for (int i = 0; i < args.length; i++) {
			logger.debug("{}: {}", i, args[i]);
		}
	}

	@Override
	public void run(String... args) throws Exception {

		

		if (businessService != null) {
			logger.info(businessService.toString());
			
			businessService.doLogic(args);
		} else {
			logger.warn("on ne veut pas en être là");
		}
		
		
		System.exit(-1);
	}

	/**
	 * Datasource XA 1 sous gestion Atomikos.
	 * 
	 * @return
	 */
	@Bean(name = "_h2DataSource_1", initMethod = "init", destroyMethod = "close")
	public DataSource h2DataSource1() {
		JdbcDataSource h2XaDataSource = new JdbcDataSource();
		h2XaDataSource.setURL(Consts.DB_URL);
		h2XaDataSource.setUser(Consts.DB_LOGIN);
		h2XaDataSource.setPassword(Consts.DB_PWD);

		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(h2XaDataSource);
		xaDataSource.setUniqueResourceName("xads1");
		xaDataSource.setPoolSize(10);

		return xaDataSource;
	}

	/**
	 * Datasource XA 2 sous gestion Atomikos.
	 * 
	 * @return
	 */
	/*
	 * @Bean(name = "_h2DataSource_2", initMethod = "init", destroyMethod = "close")
	 * public DataSource h2DataSource2() { JdbcDataSource h2XaDataSource = new
	 * JdbcDataSource(); h2XaDataSource.setURL(
	 * "jdbc:h2:file:/Users/avebertrand/git/demo/target/data/demo_2");
	 * 
	 * AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
	 * xaDataSource.setXaDataSource(h2XaDataSource);
	 * xaDataSource.setUniqueResourceName("xads2"); xaDataSource.setPoolSize(10);
	 * 
	 * return xaDataSource; }
	 */

	@Bean(name = "_userTransaction")
	public UserTransaction userTransaction() throws Throwable {
		UserTransactionImp userTransactionImp = new UserTransactionImp();
		userTransactionImp.setTransactionTimeout(10000);
		return userTransactionImp;
	}

	@Bean(name = "_atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
	public TransactionManager atomikosTransactionManager() throws Throwable {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.setForceShutdown(false);

		AtomikosJtaPlatform.transactionManager = userTransactionManager;

		return userTransactionManager;
	}

	@Bean(name = "_transactionManager")
	@DependsOn({ "_userTransaction", "_atomikosTransactionManager" })
	public PlatformTransactionManager transactionManager() throws Throwable {
		UserTransaction userTransaction = userTransaction();

		AtomikosJtaPlatform.transaction = userTransaction;

		TransactionManager atomikosTransactionManager = atomikosTransactionManager();
		return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
	}

	@Bean
	public JmsListenerContainerFactory<?> _myJMSFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message
		// converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		
		
		return factory;
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			logger.debug("-----------------------------------------------------------");
			logger.debug("-----------------------------------------------------------");
			logger.debug("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				logger.debug(beanName);
			}
			logger.debug("-----------------------------------------------------------");
			logger.debug("-----------------------------------------------------------");
		};
	}

}
