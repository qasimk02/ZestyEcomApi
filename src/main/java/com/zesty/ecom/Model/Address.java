package com.zesty.ecom.Model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id", nullable = false, updatable = false)
	private Long addressId;
	
	@Column(name="full_name",nullable=false)
	@NotBlank(message = "Full name is required")
	private String fullName;
	
	@Column(name = "phone_number",nullable=false)
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$", message = "Invalid phone number format")
	private String phoneNumber;
	
	@Column(name="postal_code",nullable=false)
	@Size(min = 6,max=6,message="Invalid Postal Code")
	private String postalCode;
	
	@Column(name="street_address",nullable=false)
	@NotBlank(message="Address is required")
	private String streetAddress;
	
	@Column(name="optional_address")
    private String optionalAddress;
	
	@Column(name="city",nullable=false)
	@NotBlank(message="City is blank")
    private String city;
	
	@Column(name="state",nullable=false)
	@NotBlank(message="State is blank")
    private String state;
	
	@Column(name="country",nullable=false)
	@NotBlank(message="Coutnry is blank")
    private String country;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy = "shippingAddress", fetch= FetchType.LAZY)
	private List<Order> order;
}
