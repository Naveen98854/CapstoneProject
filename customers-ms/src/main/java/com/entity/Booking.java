package com.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private int bookingId;

	@NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be a positive number")
	@Column(name = "customer_id")
	private int customerId;

	@NotNull(message = "Room ID cannot be null")
    @Positive(message = "Room ID must be a positive number")
	@Column(name = "room_id")
	private int roomId;

	//@FutureOrPresent(message = "Booking date must be today or in the future")
	@NotNull(message = "Booking date cannot be null")
	@Column(name = "booking_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date bookingDate;
	
	
	@NotEmpty(message = "Status cannot be empty")
	private String status;

	
	// Getters and Setters

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// Parameterized constructor
	public Booking(int customerId, int roomId, Date bookingDate, String status) {
		super();
		this.customerId = customerId;
		this.roomId = roomId;
		this.bookingDate = bookingDate;
		this.status = status;
	}

	// Deafult constructor
	public Booking() {
		super();
	}

}//End of the class
