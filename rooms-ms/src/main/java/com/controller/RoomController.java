package com.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Room;
import com.exception.RoomNotFoundException;
import com.service.RoomService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/rooms")
@Validated
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    // Get Rooms by Location
    @GetMapping
    public ResponseEntity<List<Room>> getRoomsByLocation(@RequestParam("location") @NotEmpty(message = "Location cannot be empty") String location) {
        logger.info("Request received to get rooms by location: {}", location);
        List<Room> rooms = roomService.findRoomsByLocation(location);
        if (rooms.isEmpty()) {
            logger.warn("No rooms found in the specified location: {}", location);
            throw new RoomNotFoundException("No rooms found in the specified location: " + location);
        }
        logger.info("Returning {} rooms for location: {}", rooms.size(), location);
        return ResponseEntity.ok(rooms);
    }

    // Get Room Details by ID
    @GetMapping("/{room_id}")
    public ResponseEntity<Room> getRoomDetailsById(
            @PathVariable("room_id") @Positive @Min(value = 0, message = "Room Id must be positive") int roomId) {
        logger.info("Request received to get room details for room ID: {}", roomId);
        Room room = roomService.findRoomById(roomId);
        if (room == null) {
            logger.warn("Room not found with ID: {}", roomId);
            throw new RoomNotFoundException("Room not found with ID: " + roomId);
        }
        logger.info("Returning details for room ID: {}", roomId);
        return ResponseEntity.ok(room);
    }
}
