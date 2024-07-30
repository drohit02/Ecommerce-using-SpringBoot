package com.ecommerce.ecom.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecom.custom_exception.ResourceNotFoundException;
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

	@Override
	public List<AddressDTO> loadAllAddresses() {
		List<Address> addresses = this.addressRepository.findAll();
		if(addresses.isEmpty())
			throw new ResourceNotFoundException("No addresses are found!!");
		List<AddressDTO> addressDTOs = addresses.stream().map(item->modelMapper.map(item, AddressDTO.class)).toList();
		return addressDTOs;
	}

	@Override
	public AddressDTO loadAddressById(Long addressId) {
		Address address = this.addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address ", "addressId ", addressId));
		return modelMapper.map(address, AddressDTO.class);
	}

}
