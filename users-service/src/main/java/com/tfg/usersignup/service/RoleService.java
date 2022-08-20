package com.tfg.usersignup.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.usersignup.enums.RoleName;
import com.tfg.usersignup.model.entity.Role;
import com.tfg.usersignup.repository.RoleRepository;

@Service
@Transactional
public class RoleService {
	
	@Autowired
	RoleRepository repository;
	
	public Optional<Role> getByRoleName(RoleName roleName) {
		return repository.findByRoleName(roleName);
	}
	
	public void save(Role role) {
		repository.save(role);
	}

}
