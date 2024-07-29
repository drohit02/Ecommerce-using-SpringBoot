package com.ecommerce.ecom.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.dto.AddressDTO;
import com.ecommerce.ecom.model.User;
import com.ecommerce.ecom.service.AddressService;
import com.ecommerce.ecom.utils.AuthUtils;

@RestController
@RequestMapping("/api")
public class AddressController {
	
	@Autowired
	private AddressService addressService;

	@Autowired
	private AuthUtils authUtils;
	
	@PostMapping("/addresses")
	public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
		User user = this.authUtils.loggedInUser();
		AddressDTO savedAddress = this.addressService.addAddress(user,addressDTO);
		return new ResponseEntity<>(savedAddress,HttpStatus.OK);
		
	}
}
