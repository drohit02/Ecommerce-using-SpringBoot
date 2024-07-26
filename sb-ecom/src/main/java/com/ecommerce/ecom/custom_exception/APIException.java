package com.ecommerce.ecom.custom_exception;

public class APIException extends RuntimeException{
	 	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public APIException() {
		super();
	}
	public APIException(String msg) {
		super(msg);
	}

}
