package com.chat.chat_app.controller;

import com.chat.chat_app.entities.Message;
import com.chat.chat_app.entities.Room;
import com.chat.chat_app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId){
        if(roomRepository.findByRoomId(roomId)!=null){
            return ResponseEntity.badRequest().body("Room Already Exists");
        }

        Room room=new Room();
        room.setRoomId(roomId);
        Room savedRoom=roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?>joinRoom(@PathVariable String roomId){
        Room room=roomRepository.findByRoomId(roomId);
        if(room==null){
            return ResponseEntity.badRequest().body("Room not found");
        }
        return ResponseEntity.ok(room);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>>getMessage(@PathVariable String roomId,
                                                   @RequestParam(value = "page",defaultValue = "0",required = false)int page,
                                                   @RequestParam(value = "size",defaultValue = "20",required = false)int size){
        Room room=roomRepository.findByRoomId(roomId);
        if(room==null){
            return  ResponseEntity.badRequest().build();
        }
        List<Message>messages=room.getMessages();
        int start=Math.max(0,messages.size()-(page+1)*size);
        int end=Math.min(messages.size(),start+size);
        List<Message> paginatedMessages=messages.subList(start,end);
        return ResponseEntity.ok(paginatedMessages);

    }
}
