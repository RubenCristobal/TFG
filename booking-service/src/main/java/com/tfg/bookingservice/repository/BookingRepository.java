package com.tfg.bookingservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.bookingservice.model.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findAll();
	
	Optional<List<Booking>> findByStudentName(String studentName);
	
	Optional<List<Booking>> findByTeacherName(String studentName);

	Optional<Long> countByStudentName(String studentName);

	Optional<Long> countByTeacherName(String teacherName);

	Optional<Booking> findFirstByStudentNameOrderByDateAscTimeAsc(String studentName);

	Optional<Booking> findFirstByTeacherNameOrderByDateAscTimeAsc(String teacherName);

}
