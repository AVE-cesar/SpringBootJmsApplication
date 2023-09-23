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

	static TransactionManager transactionManager;
	static UserTransaction transaction;

	protected TransactionManager locateTransactionManager() {
		return transactionManager;
	}

	protected UserTransaction locateUserTransaction() {
		return transaction;
	}
}