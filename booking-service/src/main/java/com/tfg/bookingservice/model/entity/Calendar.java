package com.tfg.bookingservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity(name = "Calendar")
public class Calendar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "teacher_name", nullable = false, unique = true, columnDefinition = "VARCHAR(100)")
	private String teacherName;

	@Column(name = "lunes", nullable = false, columnDefinition = "BOOLEAN")
	private Boolean lunes;
	
	@Column(name = "martes", nullable = false, columnDefinition = "BOOLEAN")
	private Boolean martes;
	
	@Column(name = "miercoles", nullable = false, columnDefinition = "BOOLEAN")
	private Boolean miercoles;
	
	@Column(name = "jueves", nullable = false, columnDefinition = "BOOLEAN")
	private Boolean jueves;

	@Column(name = "viernes", nullable = false, columnDefinition = "BOOLEAN")
	private Boolean viernes;

}
