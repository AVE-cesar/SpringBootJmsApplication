package com.example.demo;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * ???????
 * 
 * @author avebertrand
 *
 */
public class AtomikosJtaPlatform {

	private static final long serialVersionUID = 1L;

	static TransactionManager transactionManager;
	static UserTransaction transaction;

	
	protected TransactionManager locateTransactionManager() {
		return transactionManager;
	}

	
	protected UserTransaction locateUserTransaction() {
		return transaction;
	}
}