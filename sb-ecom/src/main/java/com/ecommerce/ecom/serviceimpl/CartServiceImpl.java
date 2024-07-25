package com.ecommerce.ecom.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecom.custom_exception.APIException;
import com.ecommerce.ecom.custom_exception.ResourceNotFoundException;
import com.ecommerce.ecom.dto.CartDTO;
import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.model.Cart;
import com.ecommerce.ecom.model.CartItems;
import com.ecommerce.ecom.model.Product;
import com.ecommerce.ecom.repository.CartItemRepository;
import com.ecommerce.ecom.repository.CartRepository;
import com.ecommerce.ecom.repository.ProductRepository;
import com.ecommerce.ecom.service.CartService;
import com.ecommerce.ecom.utils.AuthUtils;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private AuthUtils authUtils;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CartDTO addProductToCart(Long productId, Integer quantity) {

		// 1. Finding existing cart or create one
		Cart cart = createCart();

		// 2. Retrieve Product Details
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		// Check if the product already exists in the cart
		CartItems cartItem = this.cartItemRepository.findCartItemsByProductIdAndCartId(cart.getCartId(), productId);

		// 3. Perform Validations
		if (cartItem != null) {
			throw new APIException("Product " + product.getProductName() + " already exists in cart!!!");
		}
		if (product.getQuantity() == 0) {
			throw new APIException(product.getProductName() + " is not available");
		}
		if (quantity > product.getQuantity()) {
			throw new APIException("Please, make an order of " + product.getProductName()
					+ " less than or equal to quantity " + product.getQuantity() + ".");
		}

		// 4. Create Cart Item
		CartItems newCartItem = new CartItems();
		newCartItem.setProduct(product);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
		newCartItem.setProductPrice(product.getSpecialPrice());
		newCartItem.setDiscount(product.getDiscount());

		// 5. Save cart Item
		CartItems savedCartItem = this.cartItemRepository.save(newCartItem);

		// Update product quantity
		product.setQuantity(product.getQuantity() - quantity);
		this.productRepository.save(product);

		// Update cart total price
		cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
		this.cartRepository.save(cart);

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

		List<CartItems> cartItems = cart.getCartItems();

		// Map cart items to ProductDTO
		List<ProductDTO> productDTOList = cartItems.stream().map(item -> {
			ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
			map.setQuantity(item.getQuantity());
			return map;
		}).collect(Collectors.toList());

		// 6. Return updated cart
		cartDTO.setProducts(productDTOList);
		return cartDTO;
	}

	@Override
	public List<CartDTO> findAllCarts() {
		List<Cart> carts = this.cartRepository.findAll();
		if (carts.isEmpty()) {
			throw new APIException("Cart is empty!!!");
		}

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
			List<ProductDTO> products = cart.getCartItems().stream().map(item -> {
				ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
				productDTO.setQuantity(item.getQuantity());
				return productDTO;
			}).toList();
			cartDTO.setProducts(products);
			return cartDTO;
		}).toList();
		return cartDTOs;
	}
	
	
	@Override
	public CartDTO findUserCartByUserId(String email,Long cartId) {
		Cart cart = this.cartRepository.findByEmailIdAndCartId(email,cartId);
		if(cart==null) 
			throw new ResourceNotFoundException("Cart not found with the email "+email);
	  CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
	  
	  List<ProductDTO> products = cart.getCartItems().stream()
			  .map(product->modelMapper.map(product, ProductDTO.class)).toList();
	  cartDTO.setProducts(products);
	  return cartDTO;
	}
	
	
	

	/*----------------------------------Helper Method Area------------------------------*/

	private Cart createCart() {
		Cart userCart = this.cartRepository.findCartByEmail(authUtils.loggedInEmail()).get();
		if (userCart != null) {
			return userCart;
		}
		Cart cart = new Cart();
		cart.setTotalPrice(0.0);
		cart.setUser(authUtils.loggedInUser());
		Cart savedUserCart = this.cartRepository.save(cart);
		return savedUserCart;
	}

	

}
