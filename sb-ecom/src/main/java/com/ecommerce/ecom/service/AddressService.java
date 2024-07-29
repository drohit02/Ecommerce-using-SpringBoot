package com.ecommerce.ecom.service;

import java.util.List;

import com.ecommerce.ecom.dto.AddressDTO;
import com.ecommerce.ecom.model.User;

public interface AddressService {

	AddressDTO addAddress(User user, AddressDTO addressDTO);

	List<AddressDTO> loadAllAddresses();

}