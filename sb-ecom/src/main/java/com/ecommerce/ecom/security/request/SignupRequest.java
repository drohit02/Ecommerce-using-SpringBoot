package com.ecommerce.ecom.security.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
	
	@NotBlank
	@Size(min = 3,max = 20)
	private String username;

	@NotBlank
	@Size(min = 6,max = 30)
	private String password;
	
	@Email
	@NotBlank
	@Size(max = 50)
	private String email;
	
	private Set<String> roles;

}
