package com.ecommerce.ecom.security.authcontroller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.app_enum.AppRole;
import com.ecommerce.ecom.custom_error_response.MessageResponse;
import com.ecommerce.ecom.model.Role;
import com.ecommerce.ecom.model.User;
import com.ecommerce.ecom.repository.RoleRepository;
import com.ecommerce.ecom.repository.UserRepository;
import com.ecommerce.ecom.security.jwt.JwtUtils;
import com.ecommerce.ecom.security.request.LoginRequest;
import com.ecommerce.ecom.security.request.SignupRequest;
import com.ecommerce.ecom.security.response.UserInfoResponse;
import com.ecommerce.ecom.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException exception) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		UserInfoResponse response = new UserInfoResponse(userDetails.getUserId(), jwtToken, userDetails.getUsername(),
				roles);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (this.userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse(LocalDateTime.now(), "User is alredy exist!!!"));
		}

		if (this.userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse(LocalDateTime.now(),"Email is already in use!!!"));
		}

		User user = new User(signUpRequest.getPassword(), passwordEncoder.encode(signUpRequest.getPassword()),
				signUpRequest.getEmail());
		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Role is not Found!!!"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = this.roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Role is Not found!!!"));
					roles.add(adminRole);
					break;

				case "seller":
					Role sellerRole = this.roleRepository.findByRoleName(AppRole.ROLE_SELLER)
							.orElseThrow(() -> new RuntimeException("Role is not found!!!!"));
					roles.add(sellerRole);
					break;

				default:
					Role userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException());
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		this.userRepository.save(user);
		return new ResponseEntity<MessageResponse>(
				new MessageResponse(LocalDateTime.now(), "User Register Successfully!!!"), HttpStatus.OK);

	}
}
