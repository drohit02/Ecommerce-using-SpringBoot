package com.ecommerce.ecom.model;

import com.ecommerce.ecom.app_enum.AppRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	
	@ToString.Exclude
	@Enumerated(EnumType.STRING)
	private AppRole roleName;
	
	
	public Role(AppRole roleName) {
		super();
		this.roleName = roleName;
	}
	
	
	
}
