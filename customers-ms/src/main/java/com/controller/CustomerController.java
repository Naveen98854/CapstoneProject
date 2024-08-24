package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Customer;
import com.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
    private CustomerService customerService;

	//Registering a new customer
	@PostMapping
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        Customer registeredCustomer = customerService.registerCustomer(customer);
        return ResponseEntity.ok(registeredCustomer);
    }
	
	//Logging in to book a room
	@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Customer customer) {
        Customer authenticatedCustomer = customerService.authenticateCustomer(customer.getEmail(), customer.getPassword());
        if (authenticatedCustomer != null) {
            String message = "You have successfully logged in, You can check for the rooms";

            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
	
	
}
