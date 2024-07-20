package com.assessment.retailorders.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

	public enum Status {
		SUCCESS, FAILURE
	}
	private Status status;
	private String message;
	private Object data;
}
