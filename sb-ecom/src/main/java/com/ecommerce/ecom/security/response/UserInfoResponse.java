package com.ecommerce.ecom.security.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

	private Long id;
	private String jwtToken;
	private String username;
	private List<String> roles;

}
