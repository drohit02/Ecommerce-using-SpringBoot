package com.ecommerce.ecom.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecom.custom_exception.ResourceNotFoundException;
import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.model.Category;
import com.ecommerce.ecom.model.Product;
import com.ecommerce.ecom.payload.ProductResponse;
import com.ecommerce.ecom.repository.CategoryRepository;
import com.ecommerce.ecom.repository.ProductRepository;
import com.ecommerce.ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public ProductResponse getAllProducts() {

		List<Product> products = this.productRepository.findAll();
		if (products.isEmpty())
			throw new ResourceNotFoundException("Product List is empty");
		return mapProductResponse(products);
	}
	
	@Override
	public ProductResponse getProductWithCategoryId(Long categoryId) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
		List<Product> products= this.productRepository.findAllByCategory(category);
		if (products.isEmpty())
			throw new ResourceNotFoundException("Product list is empty");
		return mapProductResponse(products);
	}
	
	@Override
	public ProductResponse getProductWithKeyword(String keyword) {
		List<Product> products = this.productRepository.findAllByProductNameLikeIgnoreCase('%'+keyword+'%');
		if(products.isEmpty())
			throw new ResourceNotFoundException("Product with the "+keyword+" is not found!!!");
		return mapProductResponse(products);
	}

	@Override
	public ProductDTO addProduct(Product product, Long categoryId) {
		Category category = findCategoryById(categoryId);

		product.setCategory(category);
		product.setSpecialPrice(calculateSpeciaPrize(product));
		product.setImage("default.png");
		Product saveProduct = this.productRepository.save(product);
		return modelMapper.map(saveProduct, ProductDTO.class);
	}
	
	@Override
	public ProductDTO updateExitingProductData(Product product, Long productId) {
		Product savedProduct = this.productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
		Product updatedProduct = updateProductData(savedProduct, product);
		return modelMapper.map(updatedProduct, ProductDTO.class);
	}
	

	@Override
	public String deleteProductBy(Long productId) {
		Product product = this.productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product","Productid",productId));
		this.productRepository.deleteById(product.getProductId());
		return "Product Removed Successfully";
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
	
	private Product updateProductData(Product savedProduct,Product product) {
		savedProduct.setProductName(product.getProductName());
		savedProduct.setDescription(product.getDescription());
		savedProduct.setPrice(product.getPrice());
		savedProduct.setDiscount(product.getDiscount());
		savedProduct.setQuantity(product.getQuantity());
		savedProduct.setSpecialPrice(calculateSpeciaPrize(product));
		Product updatedProduct = this.productRepository.save(savedProduct);
		return updatedProduct;
	}

	
}
