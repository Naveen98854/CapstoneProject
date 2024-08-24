package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Room;
import com.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
    private RoomService roomService;
	
	@GetMapping
    public ResponseEntity<List<Room>> getRoomsByLocation(@RequestParam("location") String location) {
        List<Room> rooms = roomService.findRoomsByLocation(location);
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(rooms);
        }
    }
	
	
	@GetMapping("/{room_id}")
    public ResponseEntity<Room> getRoomDetailsById(@PathVariable("room_id") int roomId) {
        Room room = roomService.findRoomById(roomId);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	
}//End of the class
