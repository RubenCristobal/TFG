package com.tfg.usersignup.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.usersignup.enums.RoleName;
import com.tfg.usersignup.model.dto.JwtDto;
import com.tfg.usersignup.model.dto.LoginUser;
import com.tfg.usersignup.model.dto.NewUser;
import com.tfg.usersignup.model.entity.Role;
import com.tfg.usersignup.model.entity.User;
import com.tfg.usersignup.repository.UsersRepository;
import com.tfg.usersignup.security.jwt.JwtProvider;

@Service
@Transactional
public class UserService {
	
	@Autowired
	UsersRepository repository;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	public void save(NewUser newUser,PasswordEncoder encoder) {
		User user = new User();
		user.setLoginName(newUser.getLoginName());
		user.setEmail(newUser.getEmail());
		user.setPassword(encoder.encode(newUser.getPassword()));
		user.setFirstName(newUser.getFirstName());
		user.setLastName(newUser.getLastName());
		
		Set<Role> roles = new HashSet<>();
		if(newUser.getRoles().contains("admin")) {
			roles.add(roleService.getByRoleName(RoleName.ADMIN).get());
		}
		if(newUser.getRoles().contains("teacher")) {
			roles.add(roleService.getByRoleName(RoleName.TEACHER).get());
		}
		if(newUser.getRoles().contains("student")) {
			roles.add(roleService.getByRoleName(RoleName.STUDENT).get());
		}
		user.setRoles(roles);
		repository.save(user);
	}
	
	public JwtDto createJwtDto(LoginUser loginUser, AuthenticationManager authenticationManager) {
		Authentication authentication =
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getLoginName(), loginUser.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto();
        jwtDto.setToken(jwt);
        jwtDto.setUserName(userDetails.getUsername());
        jwtDto.setAuthorities(userDetails.getAuthorities());
        
        return jwtDto;
	}
	
	public Optional<User> getLoginName(String loginName) {
		return repository.findByLoginName(loginName);
	}
	
	public boolean existsByLoginName(String loginName) {
		return repository.existsByLoginName(loginName);
	}
	
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}
	
	public boolean existsById(Long id) {
		return repository.existsById(id);
	}
	
	public Optional<User> findById(Long id) {
		return repository.findById(id);
	}
	
	public List<User> findAll() {
		return repository.findAll();
	}
	
	public List<User> findStudents() {
		return repository.findStudents();
	}
	
	public List<User> findTeachers() {
		return repository.findTeachers();
	}

}
