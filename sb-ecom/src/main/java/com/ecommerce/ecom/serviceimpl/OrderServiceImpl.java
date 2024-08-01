package com.ecommerce.ecom.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecom.custom_exception.APIException;
import com.ecommerce.ecom.custom_exception.ResourceNotFoundException;
import com.ecommerce.ecom.dto.OrderDTO;
import com.ecommerce.ecom.model.Address;
import com.ecommerce.ecom.model.Cart;
import com.ecommerce.ecom.model.CartItems;
import com.ecommerce.ecom.model.Order;
import com.ecommerce.ecom.model.OrderItem;
import com.ecommerce.ecom.model.Payment;
import com.ecommerce.ecom.repository.AddressRepository;
import com.ecommerce.ecom.repository.CartRepository;
import com.ecommerce.ecom.repository.OrderItemRepository;
import com.ecommerce.ecom.repository.OrderRepository;
import com.ecommerce.ecom.repository.PaymentRepository;
import com.ecommerce.ecom.service.CartService;
import com.ecommerce.ecom.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CartService cartService;

	@Transactional
	@Override
	public OrderDTO placeOrder(String emailId, String paymentMethod, Long addressId, String pgName, String pgPaymentId,
			String pgStatus, String pgResponseMessage) {
		Cart cart = this.cartRepository.findCartByEmail(emailId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "email-id", emailId));

		Address address = this.addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
		
		Order order = new Order();
		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());
		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted!!");
		order.setAddress(address);
		
		Payment payment = new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
		payment.setOrder(order);
		payment = this.paymentRepository.save(payment);
		order.setPayment(payment);
		
		Order savedOrder = this.orderRepository.save(order);
		
		List<CartItems> cartItems = cart.getCartItems();
		
		if(cartItems.isEmpty())
			throw new APIException("Cart is Empty!!");
		
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItems cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);
			orderItems.add(orderItem);
		}
		orderItems = this.orderItemRepository.saveAll(orderItems);
	
		return null;
	}

}















