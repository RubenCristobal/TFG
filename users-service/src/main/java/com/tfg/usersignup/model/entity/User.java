package com.tfg.usersignup.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	@Column(name = "id",
			updatable = false)
	private Long id;
	
	@Column(name = "login_name",
			nullable = false,
			unique = true,
			columnDefinition = "VARCHAR(100)")
	private String loginName;
	
	@Column(name = "email",
			nullable = false,
			unique = true,
			columnDefinition = "VARCHAR(100)")
	private String email;
	
	@Column(name = "password",
			nullable = false)
	private String password;
	
	@Column(name = "first_name",
			nullable = false,
			columnDefinition = "TEXT")
	private String firstName;
	
	@Column(name = "last_name",
			nullable = false,
			columnDefinition = "TEXT")
	private String lastName;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Column(name = "roles",
			nullable = false)
	private Set<Role> roles = new HashSet<>();
	
}
