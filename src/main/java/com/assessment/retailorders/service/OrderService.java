package com.assessment.retailorders.service;

import java.util.List;

import com.assessment.retailorders.entity.Order;

public interface OrderService {

	List<Order> getAllOrders();
	
	Order getOrderById(Long id);
	
	Order saveOrder(Order order);
	
	Order updateOrder(Long id, Order order);
	
	void deleteOrderById(Long id);
}
