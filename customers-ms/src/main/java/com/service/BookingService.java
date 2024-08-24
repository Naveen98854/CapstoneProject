package com.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Booking;
import com.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
    private BookingRepository bookingRepository;
	
	public void saveBooking(Booking booking) {
        
        booking.setBookingDate(new Date());
        booking.setStatus("BOOKED");
        bookingRepository.save(booking);      
    }
	
	public List<Booking> getBookingsByCustomerId(int customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }
	
}
