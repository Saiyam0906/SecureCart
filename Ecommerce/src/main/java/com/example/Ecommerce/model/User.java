package com.example.Ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.Ecommerce.enums.Role;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class User {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "First name is required")
    @Size(max = 50)
    @Column(name = "first_name", nullable = false)
	private String firstName;
	
	
	@NotBlank(message = "Last name is required")
	@Size(max = 50)
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 3, message = "Password must be at least 3 characters")
	@Column(nullable = false)
	private String password;
		
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
	
	 @Column(nullable = false)
	 private boolean enabled = true;

	 @Column(nullable = false)
	 private boolean accountLocked = false;
	 
	 @Column(name = "failed_attempts", nullable = false)
	 private int failedAttempts = 0;
	 
	 @Column(name = "lock_time")
	    private LocalDateTime lockTime;
	 
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();
	 
	@Column(length=10)
    private String phoneNumber;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

    @UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	private Cart cart;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Order> orders=new ArrayList<Order>();
	
	
	
	
	
}
