package com.user.controller;

import com.user.dto.ApiResponse;
import com.user.dto.UserDetails;
import com.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDetails>>> getAllUsers() {
        ApiResponse<List<UserDetails>> resp = userService.getAllUsers();
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetails>> getUserById(@PathVariable String id) {
        ApiResponse<UserDetails> resp = userService.getUserById(id);
        System.out.println("Hello");
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserDetails>> createUser(@RequestBody UserDetails userDetails) {
        ApiResponse<UserDetails> resp = userService.createUser(userDetails);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserDetails>> updateUserStatus(@PathVariable String id,
                                                                      @RequestParam String status) {
        ApiResponse<UserDetails> resp = userService.updateUserStatus(id, status);
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }
    
}
