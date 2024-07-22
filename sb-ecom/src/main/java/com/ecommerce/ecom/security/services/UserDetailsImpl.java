package com.ecommerce.ecom.security.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.ecom.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private String username;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;

	@JsonIgnore
	private String password;

	public static UserDetails buildUser(User user) {
		List<GrantedAuthority> authorities = user
			    .getRoles().stream()
			    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
			    .collect(Collectors.toList());
		
		return new UserDetailsImpl(
				user.getUserId(),
				user.getUserName(),
				user.getEmail(),
				authorities,
				user.getPassword());
		}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
