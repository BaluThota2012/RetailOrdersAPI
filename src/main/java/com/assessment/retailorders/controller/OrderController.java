package com.assessment.retailorders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.retailorders.entity.Order;
import com.assessment.retailorders.helper.ResponseHelper;
import com.assessment.retailorders.modal.OrderResponse;
import com.assessment.retailorders.modal.OrderResponse.Status;
import com.assessment.retailorders.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService orderService;
	
	@Autowired
	public OrderController(final OrderService orderService) {
		this.orderService = orderService;
	}
	
	@GetMapping
	public ResponseEntity<OrderResponse> getAllOrders() {
		return ResponseHelper.sendResponse(Status.SUCCESS, orderService.getAllOrders());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		return ResponseHelper.sendResponse(Status.SUCCESS, orderService.getOrderById(id));
	}
	
	@PostMapping
	public ResponseEntity<OrderResponse> saveOrder(@RequestBody Order order) {
		return ResponseHelper.sendResponse(Status.SUCCESS, orderService.saveOrder(order));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody Order order) {
		return ResponseHelper.sendResponse(Status.SUCCESS, orderService.updateOrder(id, order));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<OrderResponse> deleteOrderById(@PathVariable Long id) {
		orderService.deleteOrderById(id);	
		return ResponseHelper.sendResponse(Status.SUCCESS, "Order deleted successfully", HttpStatus.OK	);
	}
	
}
