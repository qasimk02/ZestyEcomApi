package com.zesty.ecom.Model;

import java.time.LocalDateTime;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@Entity
@ToString
@Table(name="review",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))//uniqueConstraint checks uniqueness of combined column for e.g (1 1) should not occur again
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="review_id",updatable=false)
	private Long reviewId;
	
	@Column(name="subject")//nullable=false
	@Size(min = 5, max = 50, message = "Review headline must be between 5 and 500 characters.")

	private String subject;
	
	@Column(name="content")//nullable=false
	@Size(min = 10, max = 500, message = "Review must be between 10 and 500 characters.")

	private String content;
	
	@Column(name="image_name")
	private String imageName;
	
	@OneToOne(cascade=CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name="rating_id")
	private Rating rating;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@Convert(converter = LocalDateTimeConverter.class)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@ManyToOne()
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_id")
	@JsonIgnore
	private Product product;
	
}
