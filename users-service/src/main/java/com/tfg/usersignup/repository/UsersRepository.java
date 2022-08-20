package com.tfg.usersignup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg.usersignup.model.entity.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
	
	Optional<User> findById(Long id);

	Optional<User> findByLoginName(String loginName);
	
	List<User> findAll();
	
	@Query(value = "SELECT u.* \n"
			+ "FROM user u\n"
			+ "INNER JOIN user_role ur ON ur.user_id = u.id \n"
			+ "INNER JOIN role r ON r.id = ur.role_id \n"
			+ "WHERE r.role_name IN (\"STUDENT\")",
			nativeQuery = true)
	List<User> findStudents();
	
	@Query(value = "SELECT u.* \n"
			+ "FROM user u\n"
			+ "INNER JOIN user_role ur ON ur.user_id = u.id \n"
			+ "INNER JOIN role r ON r.id = ur.role_id \n"
			+ "WHERE r.role_name IN (\"TEACHER\")",
			nativeQuery = true)
	List<User> findTeachers();
	
	boolean existsByLoginName(String userName);
	
	boolean existsByEmail(String email);

}
