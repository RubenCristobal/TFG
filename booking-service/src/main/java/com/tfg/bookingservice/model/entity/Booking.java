package com.tfg.bookingservice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.tfg.bookingservice.enums.FormatoTutoria;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity(name = "Booking")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "student_name", "teacher_name", "date", "time" }) })
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	@Column(name = "id", updatable = false)
	private Long id;

	@Column(name = "student_name", nullable = false, columnDefinition = "VARCHAR(100)")
	private String studentName;

	@Column(name = "teacher_name", nullable = false, columnDefinition = "VARCHAR(100)")
	private String teacherName;

	@Column(name = "date", nullable = false, columnDefinition = "DATE")
	private String date;

	@Column(name = "time", nullable = false, columnDefinition = "time")
	private String time;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "format", nullable = false, columnDefinition = "VARCHAR(100)")
	private FormatoTutoria format;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;

}
