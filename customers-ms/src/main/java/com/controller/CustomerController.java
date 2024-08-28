package com.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Customer;
import com.entity.Room;
import com.exception.CustomException;
import com.service.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody Customer customer) {
        logger.info("Received request to register customer with email: {}", customer.getEmail());
        try {
            Customer registeredCustomer = customerService.registerCustomer(customer);
            logger.info("Customer registered successfully with ID: {}", registeredCustomer.getCustomerId());
            return ResponseEntity.ok(registeredCustomer);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                String errorMessage = "Registration failed: Duplicate entry '" + customer.getEmail() + "' for key 'customers email'";
                logger.error(errorMessage);
                throw new CustomException(errorMessage, "REGISTRATION_ERROR");
            }
            logger.error("Registration failed due to database error: {}", ex.getMessage());
            throw new CustomException("Registration failed due to database error", "REGISTRATION_ERROR");
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            throw new CustomException("Registration failed: " + e.getMessage(), "REGISTRATION_ERROR");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Customer customer) {
        logger.info("Received login request for email: {}", customer.getEmail());
        try {
            Customer authenticatedCustomer = customerService.authenticateCustomer(customer.getEmail(), customer.getPassword());
            if (authenticatedCustomer != null) {
                String message = "You have successfully logged in, You can check for the rooms";
                logger.info("Login successful for email: {}", customer.getEmail());
                return ResponseEntity.ok(message);
            } else {
                logger.warn("Login failed for email: {}", customer.getEmail());
                throw new CustomException("Invalid email or password", "AUTH_ERROR");
            }
        } catch (CustomException e) {
            logger.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            throw new CustomException("An unexpected error occurred: " + e.getMessage(), "GENERAL_ERROR");
        }
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getRoomsByLocation(@RequestParam("location") @NotEmpty(message = "Location cannot be empty") String location) {
        logger.info("Received request to get rooms by location: {}", location);
        List<Room> rooms = customerService.findRoomsByLocation(location);
        logger.info("Found {} rooms for location: {}", rooms.size(), location);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Room> getRoomDetailsById(@PathVariable("roomId") @Positive int roomId) {
        logger.info("Received request to get room details for room ID: {}", roomId);
        Room room = customerService.findRoomById(roomId);
        if (room != null) {
            logger.info("Found room details for room ID: {}", roomId);
            return ResponseEntity.ok(room);
        } else {
            logger.warn("Room not found for room ID: {}", roomId);
            throw new CustomException("Room not found for room ID: " + roomId, "ROOM_NOT_FOUND");
        }
    }
}
