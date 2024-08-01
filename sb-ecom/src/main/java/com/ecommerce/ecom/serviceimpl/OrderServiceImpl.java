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
import com.ecommerce.ecom.dto.OrderItemDTO;
import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.model.Address;
import com.ecommerce.ecom.model.Cart;
import com.ecommerce.ecom.model.CartItems;
import com.ecommerce.ecom.model.Order;
import com.ecommerce.ecom.model.OrderItem;
import com.ecommerce.ecom.model.Payment;
import com.ecommerce.ecom.model.Product;
import com.ecommerce.ecom.repository.AddressRepository;
import com.ecommerce.ecom.repository.CartRepository;
import com.ecommerce.ecom.repository.OrderItemRepository;
import com.ecommerce.ecom.repository.OrderRepository;
import com.ecommerce.ecom.repository.PaymentRepository;
import com.ecommerce.ecom.repository.ProductRepository;
import com.ecommerce.ecom.service.CartService;
import com.ecommerce.ecom.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	CartService cartService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ProductRepository productRepository;

	@Override
	@Transactional
	public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId,
			String pgStatus, String pgResponseMessage) {
		Cart cart = cartRepository.findCartByEmail(emailId).get();
		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "email", emailId);
		}

		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

		Order order = new Order();
		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());
		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted !");
		order.setAddress(address);

		Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
		payment.setOrder(order);
		payment = paymentRepository.save(payment);
		order.setPayment(payment);

		Order savedOrder = orderRepository.save(order);

		List<CartItems> cartItems = cart.getCartItems();
		if (cartItems.isEmpty()) {
			throw new APIException("Cart is empty");
		}

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

		orderItems = orderItemRepository.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			Integer quantity = item.getQuantity();
			Product product = item.getProduct();

			// Reduce stock quantity
			product.setQuantity(product.getQuantity() - quantity);

			// Save product back to the database
			productRepository.save(product);

			// Remove items from cart
			cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		if (orderDTO.getOrderItems() == null) {
			orderDTO.setOrderItems(new ArrayList<>()); // Ensure the list is initialized
		}
		 orderItems.forEach(item -> {
	            OrderItemDTO orderItemDTO = modelMapper.map(item, OrderItemDTO.class);
	            orderItemDTO.setQuantity(item.getQuantity()); // Ensure ordered quantity is set

	            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
	            productDTO.setQuantity(item.getQuantity()); // Set the ordered quantity
	            orderItemDTO.setProduct(productDTO);
	            
	            orderDTO.getOrderItems().add(orderItemDTO);
	        });
		return orderDTO;
	}
}
