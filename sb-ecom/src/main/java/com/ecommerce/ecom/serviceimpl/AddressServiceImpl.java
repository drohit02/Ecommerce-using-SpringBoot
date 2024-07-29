package com.ecommerce.ecom.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecom.dto.AddressDTO;
import com.ecommerce.ecom.model.Address;
import com.ecommerce.ecom.model.User;
import com.ecommerce.ecom.repository.AddressRepository;
import com.ecommerce.ecom.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public AddressDTO addAddress(User user, AddressDTO addressDTO) {
		Address address = this.modelMapper.map(addressDTO, Address.class);
		List<Address> addresses = user.getAddresses();
		addresses.add(address);
		user.setAddresses(addresses);
		address.setUser(user);
		Address savedAddress = this.addressRepository.save(address);
		return this.modelMapper.map(savedAddress, AddressDTO.class);
	}

}
