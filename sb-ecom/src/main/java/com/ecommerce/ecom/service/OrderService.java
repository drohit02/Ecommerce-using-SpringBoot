package com.ecommerce.ecom.service;

import com.ecommerce.ecom.dto.OrderDTO;

public interface OrderService {

	
	OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId,
			String pgStatus, String pgResponseMessage);

	
}
