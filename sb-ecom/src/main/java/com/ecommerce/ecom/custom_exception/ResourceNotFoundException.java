package com.ecommerce.ecom.custom_exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

	private String resourceName;
	private String filed;
	private String filedName;
	private String msg;
	private Long filedId;

	public ResourceNotFoundException(String resourceName, String filed, String filedName) {
		super(String.format("%s not found with %s : %s", resourceName, filed, filedName));
		this.resourceName = resourceName;
		this.filed = filed;
		this.filedName = filedName;
	}

	public ResourceNotFoundException(String resourceName, String filed, Long filedId) {
		super(String.format("%s not found with %s : %d", resourceName, filed, filedId));
		this.resourceName = resourceName;
		this.filed = filed;
		this.filedId = filedId;
	}

	public ResourceNotFoundException(String msg) {
		super(msg);
		this.msg = msg;
	}
	

}
