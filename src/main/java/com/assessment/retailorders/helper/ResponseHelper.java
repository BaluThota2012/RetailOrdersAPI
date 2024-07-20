package com.assessment.retailorders.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assessment.retailorders.modal.OrderResponse;
import com.assessment.retailorders.modal.OrderResponse.Status;

public class ResponseHelper {

	private ResponseHelper() {}
	
	public static ResponseEntity<OrderResponse> sendResponse(Status status, Object data) {
		return sendResponse(status, data, HttpStatus.OK);
	}
	public static ResponseEntity<OrderResponse> sendResponse(Status status, String message, HttpStatus httpStatus) {
		return sendResponse(new OrderResponse(status, message, null), httpStatus);
	}
	public static ResponseEntity<OrderResponse> sendResponse(Status status, Object data, HttpStatus httpStatus){
		return sendResponse(new OrderResponse(status, null, data), httpStatus);
	}
	private static ResponseEntity<OrderResponse> sendResponse(OrderResponse ordersResponse, HttpStatus httpStatus) {
		return new ResponseEntity<>(ordersResponse, httpStatus);
	}
}
