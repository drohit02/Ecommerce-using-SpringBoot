package com.ecommerce.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

	private Long paymentId;
	private String paymentMethod;
	private String pgName;
	private String pgPaymentId;
	private String pgStatus;
	private String pgResponseMessage;
	
}
