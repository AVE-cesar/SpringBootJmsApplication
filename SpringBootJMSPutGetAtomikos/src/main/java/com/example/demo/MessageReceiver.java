package com.example.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class MessageReceiver {

	private final static Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

	// @JmsListener(destination = Consts.QUEUE_NAME)
	public void receiveMessage(Message msg) {
		try {
			logger.info("on a reçu une message JMS: " + ((TextMessage) msg).getText());
		} catch (JMSException e) {
			logger.error("", e);
		}
	}
}