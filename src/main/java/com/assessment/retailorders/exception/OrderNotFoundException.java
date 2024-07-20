package com.assessment.retailorders.exception;

public class OrderNotFoundException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4424898841086084307L;

	public OrderNotFoundException(Long id) {
		super(getMessage(id));
	}
	
	public OrderNotFoundException(Throwable throwable) {
		super(throwable);
	}
	
	public OrderNotFoundException(Long id, Throwable throwable) {
		super(getMessage(id), throwable);
	}
	
	private static String getMessage(Long id) {
		return String.format("Order not found with ID: %s", id);
	}
}
