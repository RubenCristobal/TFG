package com.tfg.notificationservice.model.dto;

import lombok.Data;

@Data
public class Booking {

	private String studentName;

	private String teacherName;

	private String date;

	private String time;

	private String format;

	private String description;

}
