package com.ecommerce.ecom.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users",uniqueConstraints = {
		@UniqueConstraint(columnNames = { "email" }),
		@UniqueConstraint(columnNames = { "username" }),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotBlank(message = "username is required")
	@Size(min = 5,max = 50,message = "username should be minimum 20 and maximum 50 character")
	@Column(name = "username")
	private String username;
	
	@NotBlank(message = "password is required")
	@Size(min = 8,message = "password should be minimum 8 and maximum 50 character")
	@Column(name = "password")
	private String password;
	
	@ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinTable(
			name ="user_role",
			joinColumns = @JoinColumn(referencedColumnName = "userId"),
			inverseJoinColumns = @JoinColumn(referencedColumnName = "roleId")
			)
	private Set<Role> roles;
	
	@Email
	@Size(max = 50)
	@Column(name = "email")
	private String email;

	@ToString.Exclude
	@OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
	private Set<Product> products;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
	private List<Address> addresses;
	
	@ToString.Exclude 
	@OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
	private Cart cart;
	
	public User(
			@NotBlank(message = "username is required") @Size(min = 20, max = 50, message = "username should be minimum 20 and maximum 50 character") String username,
			@NotBlank(message = "password is required") @Size(min = 8, message = "password should be minimum 8 and maximum 50 character") String password,
			@Email @Size(max = 50) String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	
}

