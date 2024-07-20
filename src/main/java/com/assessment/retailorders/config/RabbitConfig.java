package com.assessment.retailorders.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory.AddressShuffleMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.assessment.retailorders.constant.OrderConstants;
import com.assessment.retailorders.modal.RabbitProperties;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@EnableConfigurationProperties
public class RabbitConfig {

	private final RabbitProperties rabbitProperties;
	
	@Autowired
	public RabbitConfig(final RabbitProperties rabbitmqProperties) {
		this.rabbitProperties = rabbitmqProperties;
	}
	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		log.info("Rabbitmq properties: {}", rabbitProperties);
		CachingConnectionFactory ccf = new CachingConnectionFactory();
		ccf.setAddresses(rabbitProperties.getAddress());
		ccf.setAddressShuffleMode(AddressShuffleMode.INORDER);
		ccf.setVirtualHost(rabbitProperties.getVhost());
		ccf.setUsername(rabbitProperties.getUsername());
		ccf.setPassword(rabbitProperties.getPassword());
		ccf.setPublisherConfirmType(ConfirmType.CORRELATED);
		ccf.setPublisherReturns(true);
		return ccf;
	}
	
	@Bean(name = "rabbitListenerContainerFactory")
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(cachingConnectionFactory());
		factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return factory;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory());
	    template.setUsePublisherConnection(true);
	    template.setMandatory(true);
	    template.setConfirmCallback((cd, ack, cause) -> 
	    	log.info("Correlation: {}, Ack: {}, Cause: {}", cd, ack, cause)
	    );
	    template.setReturnsCallback(callback -> 
	    	log.info("Message returned: ", callback)
	    );
	    return template;
	}
	
	@Bean
	public Queue orderQueue() {
		return QueueBuilder.durable(OrderConstants.ORDER_QUEUE).build();
	}
	
	@Bean
	public Exchange orderExchange() {
		return ExchangeBuilder.directExchange(OrderConstants.ORDER_EXCHANGE).build();
	}
	
	@Bean
	public Binding orderCreatedBinding() {
		return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(OrderConstants.ORDER_CREATED_ROUTE).noargs();
	}
}
