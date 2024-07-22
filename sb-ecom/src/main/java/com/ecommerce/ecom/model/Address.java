package com.ecommerce.ecom.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	
	@NotBlank
	@Size(min = 5,message = "Street name must be atleast 5 charachers")
	private String street;
	
	@NotBlank
	@Size(min = 5,message = "Street name must be atleast 5 charachers")
	private String buildingName;
	
	@NotBlank
	@Size(min = 5,message = "Street name must be atleast 5 charachers")
	private String city;

	
	@NotBlank
	@Size(min = 5,message = "Street name must be atleast 5 charachers")
	private String state;
	
	@NotBlank
	@Size(min = 5,message = "Street name must be atleast 5 charachers")
	private String conutry;

	@NotNull
	@Size(min = 6,message = "Street name must be atleast 5 charachers")
	private int pincode;

	@ToString.Exclude
	@ManyToMany(mappedBy = "addresses")
	private List<User> users ;
}
