package com.example.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleListener implements MessageListener {

	private final static Logger logger = LoggerFactory.getLogger(ExampleListener.class);

	public void onMessage(Message message) {
		if (message instanceof TextMessage) {

			TextMessage textMessage = (TextMessage) message;
			try {
				logger.info("Réception par notre ExampleListener: {}", textMessage.getText());
			} catch (JMSException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			throw new IllegalArgumentException("Message must be of type TextMessage");
		}
	}
}