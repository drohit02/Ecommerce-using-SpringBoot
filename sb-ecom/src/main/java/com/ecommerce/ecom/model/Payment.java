package com.ecommerce.ecom.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	
	@OneToOne(mappedBy = "payment",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	private Order order;
	
	@NotBlank
	@Size(min = 4,message = "Payment method must contain at least 4 characters")
	private String paymentMethod;
	
	private String pgName;
	private String pgPaymentId;
	private String pgStatus;
	
	
	private String pgResponseMessage;
	public Payment(Long paymentId, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
		this.paymentId = paymentId;
		this.pgName = pgName;
		this.pgPaymentId = pgPaymentId;
		this.pgStatus = pgStatus;
		this.pgResponseMessage = pgResponseMessage;
	}
}