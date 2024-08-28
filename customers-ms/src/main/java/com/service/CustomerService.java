package com.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.entity.Customer;
import com.entity.Room;
import com.exception.CustomException;
import com.repository.CustomerRepository;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Registering the new customer
    public Customer registerCustomer(Customer customer) {
        logger.info("Registering customer with email: {}", customer.getEmail());
        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Customer registered successfully with ID: {}", savedCustomer.getCustomerId());
        return savedCustomer;
    }

    public Customer authenticateCustomer(String email, String password) {
        logger.info("Authenticating customer with email: {}", email);
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            logger.info("Authentication successful for email: {}", email);
            return customer;
        } else {
            logger.warn("Authentication failed for email: {}", email);
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------//

    // Retrieve the room data from the RoomMS based on the location by using restTemplate
    // Method to get list of rooms by location
    public List<Room> findRoomsByLocation(String location) {
        logger.info("Retrieving rooms by location: {}", location);
        try {
            String roomServiceUrl = "http://localhost:9009/rooms?location=" + location;
            ResponseEntity<List<Room>> response = restTemplate.exchange(
                    roomServiceUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Room>>() {});
            
            List<Room> rooms = response.getBody();
            if (rooms == null || rooms.isEmpty()) {
                logger.warn("No rooms found for location: {}", location);
                throw new CustomException("Room not found with location: " + location, "ROOM_NOT_FOUND");
            }
            logger.info("Found {} rooms for location: {}", rooms.size(), location);
            return rooms;
        } catch (Exception e) {
            logger.error("Error retrieving rooms by location: {}", e.getMessage());
            throw new CustomException("Error retrieving rooms by location: " + e.getMessage(), "ROOM_SERVICE_ERROR");
        }
    }

    // Method to get room details by room ID
    public Room findRoomById(int roomId) {
        logger.info("Retrieving room details for room ID: {}", roomId);
        try {
            String roomServiceUrl = "http://localhost:9009/rooms/" + roomId;
            ResponseEntity<Room> response = restTemplate.getForEntity(roomServiceUrl, Room.class);
            
            Room room = response.getBody();
            if (room == null) {
                logger.warn("Room not found with ID: {}", roomId);
                throw new CustomException("Room not found with ID: " + roomId, "ROOM_NOT_FOUND");
            }
            logger.info("Found room details for room ID: {}", roomId);
            return room;
        } catch (Exception e) {
            logger.error("Error retrieving room details: {}", e.getMessage());
            throw new CustomException("Error retrieving room details: " + e.getMessage(), "ROOM_SERVICE_ERROR");
        }
    }
}
