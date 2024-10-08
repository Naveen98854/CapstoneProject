package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
	    
	List<Booking> findByCustomerId(int customerId);
	
	
	}

