package com.assessment.retailorders;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.assessment.retailorders"}, exclude = {})
@EnableRabbit
public class RetailOrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetailOrdersApplication.class, args);
	}
}
