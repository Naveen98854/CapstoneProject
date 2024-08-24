package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Booking;
import com.service.BookingService;

@RestController
@RequestMapping("/customers")
public class BookingController {

	 @Autowired
	 private BookingService bookingService;
	 
	 //Method for book a room
	 @PostMapping("/bookings")
	    public ResponseEntity<String> bookRoom(@RequestBody Booking booking) {
	        bookingService.saveBooking(booking);
	        return new ResponseEntity<>("Room booked successfully!", HttpStatus.CREATED);
	    }
	 
	 //Method for get the booking details
	 @GetMapping("/{customer_id}/bookings")
	    public ResponseEntity<List<Booking>> getCustomerBookings(@PathVariable("customer_id") int customerId) {
	        List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
	        if (bookings.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        return new ResponseEntity<>(bookings, HttpStatus.OK);
	    }
	 
}
