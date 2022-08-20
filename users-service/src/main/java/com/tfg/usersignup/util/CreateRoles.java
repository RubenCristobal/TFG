//package com.tfg.usersignup.util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import com.tfg.usersignup.enums.RoleName;
//import com.tfg.usersignup.model.entity.Role;
//import com.tfg.usersignup.service.RoleService;
//
//@Component
//public class CreateRoles implements CommandLineRunner {
//
//	@Autowired
//	RoleService roleService;
//
//	@Override
//	public void run(String... args) throws Exception {
//
//		Role roleAdmin = new Role();
//		roleAdmin.setRoleName(RoleName.ADMIN);
//
//		Role roleStudent = new Role();
//		roleStudent.setRoleName(RoleName.STUDENT);
//
//		Role roleTeacher = new Role();
//		roleTeacher.setRoleName(RoleName.TEACHER);
//
//		roleService.save(roleAdmin);
//		roleService.save(roleStudent);
//		roleService.save(roleTeacher);
//
//	}
//
//}
