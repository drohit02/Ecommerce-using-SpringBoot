package com.ecommerce.ecom.custom_error_response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
	
	private LocalDateTime timestamp;
	private String message;

}
