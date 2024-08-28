package com.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.entity.Booking;
import com.entity.Customer;
import com.entity.Room;
import com.exception.CustomException;
import com.repository.BookingRepository;
import com.repository.CustomerRepository;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    public void saveBooking(Booking booking) {
        logger.info("Starting booking process for customer ID: {}", booking.getCustomerId());

        // Validate customer ID
        Customer customer = customerRepository.findById(booking.getCustomerId())
                .orElseThrow(() -> {
                    logger.error("Customer ID {} does not exist.", booking.getCustomerId());
                    return new CustomException("Customer ID " + booking.getCustomerId() + " does not exist.", "CUSTOMER_NOT_FOUND");
                });

        logger.info("Customer ID {} found. Proceeding to validate room ID: {}", booking.getCustomerId(), booking.getRoomId());

        // Validate room ID with Room microservice
        String roomServiceUrl = "http://localhost:9009/rooms/" + booking.getRoomId();
        ResponseEntity<Room> roomResponse = restTemplate.getForEntity(roomServiceUrl, Room.class);

        if (roomResponse.getStatusCode() == HttpStatus.OK && roomResponse.getBody() != null) {
            logger.info("Room ID {} found. Proceeding to book the room.", booking.getRoomId());
            // Proceed with booking since the room exists and ID is available
            booking.setBookingDate(new Date());
            booking.setStatus("BOOKED");
            bookingRepository.save(booking);
            logger.info("Room ID {} successfully booked for customer ID {}.", booking.getRoomId(), booking.getCustomerId());
        } else {
            logger.error("Room ID {} is not available or does not exist.", booking.getRoomId());
            throw new CustomException("Room ID " + booking.getRoomId() + " is not available or does not exist.", "ROOM_NOT_FOUND");
        }
    }
    
    
    public List<Booking> getBookingsByCustomerId(int customerId) {
        logger.info("Retrieving bookings for customer ID: {}", customerId);
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        if (bookings.isEmpty()) {
            logger.warn("No bookings found for customer ID: {}", customerId);
        } else {
            logger.info("Found {} bookings for customer ID: {}", bookings.size(), customerId);
        }
        return bookings;
    }
}
