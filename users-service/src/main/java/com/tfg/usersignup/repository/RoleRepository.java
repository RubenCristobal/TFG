package com.tfg.usersignup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.usersignup.enums.RoleName;
import com.tfg.usersignup.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(RoleName userRole);
	
}
