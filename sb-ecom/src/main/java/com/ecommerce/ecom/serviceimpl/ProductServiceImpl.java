package com.ecommerce.ecom.serviceimpl;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.ecom.custom_exception.APIException;
import com.ecommerce.ecom.custom_exception.ResourceNotFoundException;
import com.ecommerce.ecom.dto.CartDTO;
import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.model.Cart;
import com.ecommerce.ecom.model.Category;
import com.ecommerce.ecom.model.Product;
import com.ecommerce.ecom.payload.ProductResponse;
import com.ecommerce.ecom.repository.CartRepository;
import com.ecommerce.ecom.repository.CategoryRepository;
import com.ecommerce.ecom.repository.ProductRepository;
import com.ecommerce.ecom.service.CartService;
import com.ecommerce.ecom.service.FileService;
import com.ecommerce.ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private FileService fileService;
	@Autowired
	private CartService cartService;
	

	@Autowired
	private CartRepository cartRepository;

	/*
	 * locate the path for the upload image and fetching the value from
	 * application.properties
	 */
	@Value("${project.image}")
	String path;

	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		/* Sorting logic */
		Sort sortByAndOrder = sortProducts(sortBy, sortOrder);

		/* Pagination Logic */

		Pageable pages = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPages = this.productRepository.findAll(pages);

		List<Product> products = productPages.getContent();

		if (products.isEmpty())
			throw new ResourceNotFoundException("Product List is empty");

		ProductResponse productResponse = mapProductResponse(products);

		return setProductResponseMetaData(productPages, productResponse);
	}

	@Override
	public ProductResponse getProductWithCategoryId(Long categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortOrder) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		Sort sortByAndOrder = sortProducts(sortBy, sortOrder);

		Pageable pageData = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPages = this.productRepository.findAllByCategoryOrderByPriceAsc(category, pageData);

		List<Product> products = productPages.getContent();
		if (products.isEmpty())
			throw new APIException("Product list is empty");

		ProductResponse productResponse = mapProductResponse(products);

		return setProductResponseMetaData(productPages, productResponse);
	}

	@Override
	public ProductResponse getProductWithKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder) {
		Sort sortByAndOrder = sortProducts(sortBy, sortOrder);

		Pageable pageData = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> productPages = this.productRepository.findAllByProductNameLikeIgnoreCase('%' + keyword + '%',
				pageData);

		List<Product> products = productPages.getContent();
		if (products.isEmpty())
			throw new APIException("Product with the " + keyword + " is not found!!!");
		ProductResponse productResponse = mapProductResponse(products);
		return setProductResponseMetaData(productPages, productResponse);
	}

	@Override
	public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
		Category category = findCategoryById(categoryId);

		boolean productPresent = isProductPresent(category.getProducts(), productDTO.getProductName());

		if (productPresent) {
			Product product = productDtoToProduct(productDTO);

			product.setCategory(category);
			product.setSpecialPrice(calculateSpeciaPrize(product));
			product.setImage("default.png");
			Product saveProduct = this.productRepository.save(product);

			return productToProductDTO(saveProduct);
		} else {
			throw new ResourceNotFoundException("Product is already present in database!!");
		}

	}

	@Override
	public ProductDTO updateExitingProductData(ProductDTO productDTO, Long productId) {
		Product savedProduct = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		Product updatedProduct = updateProductData(savedProduct, productDtoToProduct(productDTO));

		List<Cart> carts = this.cartRepository.findCartsByProductId(productId);
		List<CartDTO> cartDTOs = carts.stream().map(item -> {
		    CartDTO cartDTO = modelMapper.map(item, CartDTO.class);
		    List<ProductDTO> products = item.getCartItems().stream()
		            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
		            .toList(); // Collecting the stream into a list
		    cartDTO.setProducts(products);
		    return cartDTO;
		}).toList();

		cartDTOs.forEach(cart->this.cartService.updateProductInCarts(cart.getCartId(),productId));
		return productToProductDTO(updatedProduct);
	}

	@Override
	public ProductDTO deleteProductById(Long productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Productid", productId));
		
		List<Cart> carts = this.cartRepository.findCartsByProductId(productId);
		carts.forEach(cart->cartService.deleteProductFromCart(cart.getCartId(), productId));
		
		this.productRepository.deleteById(product.getProductId());
		return productToProductDTO(product);
	}

	@Override
	public ProductDTO updateProductWithImage(Long productId, MultipartFile image) throws IOException {
		/* Find the product using productId */
		Product savedProduct = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		/*
		 * Get the filename when image get uploaded.Below method will return the
		 * filename after the image gets uploaded
		 */
		String fileName = this.fileService.uploadImage(path, image);

		/* updating the image name and save the updated details */
		savedProduct.setImage(fileName);

		Product updatedProduct = this.productRepository.save(savedProduct);
		return productToProductDTO(updatedProduct);
	}

	/*-----------------------------------HELPER METHOD AREA-----------------------------------------*/

	private Category findCategoryById(Long categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		return category;
	}

	private double calculateSpeciaPrize(Product product) {
		double specialPrize = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
		return specialPrize;
	}

	private ProductResponse mapProductResponse(List<Product> products) {
		List<ProductDTO> productList = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.toList();
		ProductResponse productResponse = modelMapper.map(productList, ProductResponse.class);
		productResponse.setProducts(productList);
		return productResponse;
	}

	private Product updateProductData(Product savedProduct, Product product) {
		savedProduct.setProductName(product.getProductName());
		savedProduct.setDescription(product.getDescription());
		savedProduct.setPrice(product.getPrice());
		savedProduct.setDiscount(product.getDiscount());
		savedProduct.setQuantity(product.getQuantity());
		savedProduct.setSpecialPrice(calculateSpeciaPrize(product));
		Product updatedProduct = this.productRepository.save(savedProduct);
		return updatedProduct;
	}

	private ProductDTO productToProductDTO(Product product) {
		return modelMapper.map(product, ProductDTO.class);
	}

	private Product productDtoToProduct(ProductDTO productDTO) {
		return modelMapper.map(productDTO, Product.class);
	}

	private boolean isProductPresent(List<Product> products, String productName) {
		boolean isFound = true;
		for (Product product : products) {
			if (product.getProductName().equals(productName)) {
				isFound = false;
				break;
			}
		}
		return isFound;
	}

	private Sort sortProducts(String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		return sortByAndOrder;

	}

	private ProductResponse setProductResponseMetaData(Page<Product> pageMetaData, ProductResponse productResponse) {
		ProductResponse returnProductResponse = new ProductResponse();
		returnProductResponse.setProducts(productResponse.getProducts());
		returnProductResponse.setPageNumber(pageMetaData.getNumber());
		returnProductResponse.setPageSize(pageMetaData.getSize());
		returnProductResponse.setTotalElements(pageMetaData.getTotalElements());
		returnProductResponse.setTotalPages(pageMetaData.getTotalPages());
		returnProductResponse.setLastPage(pageMetaData.isLast());
		return returnProductResponse;
	}

}
