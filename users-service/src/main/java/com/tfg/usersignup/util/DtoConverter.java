package com.tfg.usersignup.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tfg.usersignup.model.dto.UserInformation;
import com.tfg.usersignup.model.entity.Role;
import com.tfg.usersignup.model.entity.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class DtoConverter {
	
	public static List<UserInformation> convertUserList(List<User> usersList) {
		List<UserInformation> userInfoList = new ArrayList<UserInformation>();
		usersList.forEach(user -> userInfoList.add(convertUser(user)));
		return userInfoList;
	}
	
	public static UserInformation convertUser(User user) {
		UserInformation userInformation = new UserInformation();
		userInformation.setLoginName(user.getLoginName());
		userInformation.setEmail(user.getEmail());
		userInformation.setFirstName(user.getFirstName());
		userInformation.setLastName(user.getLastName());
		userInformation.setRoles(convertRoles(user.getRoles()));
		return userInformation;
	}
	
	private static Set<String> convertRoles(Set<Role> roles) {
		Set<String> roleNames = new HashSet<>();
		roles.forEach(role -> roleNames.add(role.getRoleName().toString()));
		return roleNames;
	}
	
	

}
