package com.ecommerce.ecom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.dto.OrderDTO;
import com.ecommerce.ecom.dto.OrderRequestDTO;
import com.ecommerce.ecom.service.OrderService;
import com.ecommerce.ecom.utils.AuthUtils;

@RestController
@RequestMapping("/api")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AuthUtils authUtils;
	
	 @PostMapping("/order/users/payments/{paymentMethod}")
	    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
	        String emailId = this.authUtils.loggedInEmail();
	        OrderDTO order = this.orderService.placeOrder(
	                emailId,
	                orderRequestDTO.getAddressId(),
	                paymentMethod,
	                orderRequestDTO.getPgName(),
	                orderRequestDTO.getPgPaymentId(),
	                orderRequestDTO.getPgStatus(),
	                orderRequestDTO.getPgResponseMessage()
	        );
	        return new ResponseEntity<>(order, HttpStatus.CREATED);
	    }

}
