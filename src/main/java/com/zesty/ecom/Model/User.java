package com.zesty.ecom.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zesty.ecom.Util.LocalDateTimeConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;

	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;

	@Column(name = "first_name", nullable = false)
	@NotBlank(message = "Fist name is required")
	private String firstName;

	@Column(name = "last_name", nullable = false)
	@NotBlank(message = "Last name is required")
	private String lastName;

	@Column(name = "about")
	private String about;

	// have to work more and add later
//	@Column(name="gender")
//	private String gender;

//	@Column(name = "date_of_birth")
//    @Past(message = "Date of birth must be in the past")
//    private LocalDate dateOfBirth;

	@Column(name = "phone_number")
	@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$", message = "Invalid phone number format")
	private String phone;

	@Column(name = "created_at", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@Convert(converter = LocalDateTimeConverter.class)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@Column(name = "is_active")
	private Boolean active;

	// mappings
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;
	
	@OneToMany(mappedBy = "user", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Address> address = new ArrayList<>();

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Cart cart;
	
	@OneToMany(mappedBy = "user",fetch=FetchType.LAZY)
	private Set<Rating> rating;
	
	@OneToMany(mappedBy = "user",fetch=FetchType.LAZY)
	private Set<Review> review;
	
	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
	private List<Order> order;
	
	@OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
	private List<OrderItem> orderItems;

}
