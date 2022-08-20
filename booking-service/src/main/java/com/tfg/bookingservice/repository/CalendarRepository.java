package com.tfg.bookingservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.bookingservice.model.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

	List<Calendar> findAll();
	
	Optional<List<Calendar>> findByTeacherName(String studentName);

}
