package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.Room;
import com.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
    private RoomRepository roomRepository;
	
	
	public List<Room> findRoomsByLocation(String location) {
        return roomRepository.findByLocation(location);
    }
	
	public Room findRoomById(int roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }
	
	
}//End of the class
