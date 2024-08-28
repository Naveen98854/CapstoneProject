package com.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Room;
import com.repository.RoomRepository;

@Service
public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findRoomsByLocation(String location) {
        logger.info("Finding rooms by location: {}", location);
        List<Room> rooms = roomRepository.findByLocation(location);
        if (rooms.isEmpty()) {
            logger.warn("No rooms found for location: {}", location);
        } else {
            logger.info("Found {} rooms for location: {}", rooms.size(), location);
        }
        return rooms;
    }

    public Room findRoomById(int roomId) {
        logger.info("Finding room by ID: {}", roomId);
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            logger.warn("Room not found with ID: {}", roomId);
        } else {
            logger.info("Found room with ID: {}", roomId);
        }
        return room;
    }
}
