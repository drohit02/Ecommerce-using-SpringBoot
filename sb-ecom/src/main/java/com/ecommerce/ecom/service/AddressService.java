package com.ecommerce.ecom.service;

import com.ecommerce.ecom.dto.AddressDTO;
import com.ecommerce.ecom.model.User;

public interface AddressService {

	AddressDTO addAddress(User user, AddressDTO addressDTO);

}
