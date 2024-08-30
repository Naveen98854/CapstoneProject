package com.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Booking;
import com.exception.CustomException;
import com.service.BookingService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@Validated
@RequestMapping("/customers")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    //Booking a room 
    @PostMapping("/bookings")
    public ResponseEntity<String> bookRoom(@Valid @RequestBody Booking booking, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Extract and return validation errors
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessages);
        }

        logger.info("Received request to book room for customer ID: {}", booking.getCustomerId());
        try {
            bookingService.saveBooking(booking);
            return new ResponseEntity<>("Room booked successfully!", HttpStatus.CREATED);
        } catch (CustomException e) {
            logger.error("Error during room booking for customer ID {}: {}", booking.getCustomerId(), e.getMessage());
            throw e; // Re-throw the custom exception
        } catch (Exception e) {
            logger.error("Unexpected error during room booking: {}", e.getMessage());
            throw new CustomException("Failed to book room: " + e.getMessage(), "BOOKING_ERROR");
        }
    }

    
    //Get the booking

    @GetMapping("/{customer_id}/bookings")
    public ResponseEntity<List<Booking>> getCustomerBookings(
            @PathVariable("customer_id") @Min(value = 1, message = "Customer ID must be greater than 0") int customerId) {
        logger.info("Received request to retrieve bookings for customer ID: {}", customerId);
        try {
            List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
            if (bookings.isEmpty()) {
                logger.warn("No bookings found for customer ID: {}", customerId);
                throw new CustomException("No bookings found for customer with ID: " + customerId, "BOOKINGS_NOT_FOUND");
            }
            logger.info("Found {} bookings for customer ID: {}", bookings.size(), customerId);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (CustomException e) {
            logger.error("Error retrieving bookings for customer ID {}: {}", customerId, e.getMessage());
            throw e; // Re-throw the custom exception 
        } catch (Exception e) {
            logger.error("Unexpected error during bookings retrieval for customer ID {}: {}", customerId, e.getMessage());
            throw new CustomException("Failed to retrieve bookings: " + e.getMessage(), "BOOKINGS_RETRIEVAL_ERROR");
        }
    }
}
