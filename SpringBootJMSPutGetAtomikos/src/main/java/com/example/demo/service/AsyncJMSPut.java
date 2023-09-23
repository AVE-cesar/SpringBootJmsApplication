package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.Consts;

@Component
public class AsyncJMSPut {

	private final static Logger logger = LoggerFactory.getLogger(AsyncJMSPut.class);

	@Autowired
	JmsTemplate jmsTemplate;

	@Async
	public void sendJMS(int i) {
		// Send a JMS message
		jmsTemplate.convertAndSend(Consts.QUEUE_NAME, "Test:" + i);
		logger.debug("envoi d'un message: {}", i);
	}
}
