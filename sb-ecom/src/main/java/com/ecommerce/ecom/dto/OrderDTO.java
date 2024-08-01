package com.ecommerce.ecom.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

	private Long orderId;
	private String email;
	private LocalDate orderDate;
	private double totalAmount;
	private String orderStatus;
	private PaymentDTO payment;
	private List<OrderItemDTO> orderItems;
	private Long addressId;

}
