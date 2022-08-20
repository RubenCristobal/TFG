package com.tfg.usersignup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tfg.usersignup.model.entity.User;
import com.tfg.usersignup.model.entity.UserSecurity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserService service;

	@Override
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        User user = service.getLoginName(loginName).get();
        return UserSecurity.build(user);
	}
	
}
