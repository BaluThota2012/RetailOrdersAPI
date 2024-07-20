package com.assessment.retailorders.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.assessment.retailorders.constant.OrderConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderListener {

	@RabbitListener(queues = {OrderConstants.ORDER_QUEUE}, containerFactory = "rabbitListenerContainerFactory")
	public void listen(String message) {
		log.info("Order confirmed with ID: {}", message);
	}
}
