package com.ecommerce.ecom.custom_exception;

public class APIException extends RuntimeException{
	 	
	public APIException() {
		super();
	}
	public APIException(String msg) {
		super(msg);
	}

}
