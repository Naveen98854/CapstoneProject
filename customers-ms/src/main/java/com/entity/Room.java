package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int roomId;

    @Column(name = "location")
    @NotEmpty(message = "Location cannot be empty")
    private String location;

    @Column(name = "description")
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @Column(name = "room_type")
    @NotEmpty(message = "Room type cannot be empty")
    private String roomType;

    @Column(name = "price")
    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private double price;

    @Column(name = "availability_status")
    @NotEmpty(message = "Availability status cannot be empty")
    private String availabilityStatus = "Available";

    // Getters and Setters------------------------------------------------------------

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    //Parameterized constructor-------------------------------------------------
    
	public Room(String location, String description, String roomType, double price, String availabilityStatus) {
		super();
		this.location = location;
		this.description = description;
		this.roomType = roomType;
		this.price = price;
		this.availabilityStatus = availabilityStatus;
	}

	//Default constrcutor-------------------------------------------------------
	
	public Room() {
		super();
	}
    
    
    
}//End of the class
