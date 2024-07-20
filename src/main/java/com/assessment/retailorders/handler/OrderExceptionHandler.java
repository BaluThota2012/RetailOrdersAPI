package com.assessment.retailorders.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.assessment.retailorders.exception.OrderNotFoundException;
import com.assessment.retailorders.helper.ResponseHelper;
import com.assessment.retailorders.modal.OrderResponse;
import com.assessment.retailorders.modal.OrderResponse.Status;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class OrderExceptionHandler {

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<OrderResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseHelper.sendResponse(Status.FAILURE, "Invalid OrderID entered!", HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<OrderResponse> missingPathVariableExceptionHandler(MissingPathVariableException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseHelper.sendResponse(Status.FAILURE, ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<OrderResponse> requestMethodNotFoundException(HttpRequestMethodNotSupportedException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseHelper.sendResponse(Status.FAILURE, ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
	}
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<OrderResponse> orderExceptionHandler(OrderNotFoundException orderException) {
		log.error(orderException.getMessage(), orderException);
		return ResponseHelper.sendResponse(Status.FAILURE, orderException.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<OrderResponse> genericExceptionHandler(Exception ex) {
		log.error(ex.getMessage(), ex);
		return ResponseHelper.sendResponse(Status.FAILURE, "Server error. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
