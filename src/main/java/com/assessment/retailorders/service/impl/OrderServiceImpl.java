package com.assessment.retailorders.service.impl;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.retailorders.constant.OrderConstants;
import com.assessment.retailorders.entity.Order;
import com.assessment.retailorders.exception.OrderNotFoundException;
import com.assessment.retailorders.repository.OrderRepository;
import com.assessment.retailorders.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository ordersRepository;
	private final RabbitTemplate rabbitTemplate;
	
	@Autowired
	public OrderServiceImpl(final OrderRepository orderRepository, final RabbitTemplate rabbitTemplate) {
		this.ordersRepository = orderRepository;
		this.rabbitTemplate = rabbitTemplate;
	}
	@Override
	public List<Order> getAllOrders() {
		return ordersRepository.findAll();
	}

	@Override
	public Order getOrderById(Long id) {
		return ordersRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
	}

	@Override
	public Order saveOrder(Order order) {
		Order newOrder = ordersRepository.save(order);
		rabbitTemplate.convertAndSend(OrderConstants.ORDER_EXCHANGE, OrderConstants.ORDER_CREATED_ROUTE, newOrder.getId());
		return newOrder;
	}
	
	@Override
	public Order updateOrder(Long id, Order orderDetails) {
		Order order =  getOrderById(id);
		order.setCustomerName(orderDetails.getCustomerName());
		order.setOrderDate(orderDetails.getOrderDate());
		order.setStatus(orderDetails.getStatus());
		order.setTotalAmount(orderDetails.getTotalAmount());
		return ordersRepository.save(order);
	}

	@Override
	public void deleteOrderById(Long id) {
		getOrderById(id);
		ordersRepository.deleteById(id);
	}

}
