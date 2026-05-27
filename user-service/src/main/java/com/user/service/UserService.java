package com.user.service;

import com.user.dto.ApiResponse;
import com.user.dto.UserDetails;
import com.user.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final KafkaProducer kafkaProducer;

    public UserService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public ApiResponse<List<UserDetails>> getAllUsers() {
        try {
            List<UserDetails> users = new ArrayList<>();
            users.add(new UserDetails("USER_001", "John Doe", "jdoe", "Procurement_officer", "jdoe@example.com",
                    "+1234567890", "123 Main St", null, "ACTIVE", "INTERNAL", 0,
                    null, "2026-05-01T08:00:00", "admin", "2026-05-10T08:00:00", "admin"));
            users.add(new UserDetails("USER_002", "Jane Smith", "jsmith", "Finance_Manager", "jsmith@example.com",
                    "+1234567891", "456 Market St", null, "ACTIVE", "INTERNAL", 0,
                    null, "2026-05-01T08:00:00", "admin", "2026-05-10T08:00:00", "admin"));
            return ApiResponse.success(users, "Users retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving users", e);
            return ApiResponse.internalError("Error retrieving users");
        }
    }

    public ApiResponse<UserDetails> getUserById(String id) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("User id is required");
            }
            UserDetails user = new UserDetails(id, "John Doe", "jdoe", "Procurement_officer", "jdoe@example.com",
                    "+1234567890", "123 Main St", null, "ACTIVE", "INTERNAL", 0,
                    null, "2026-05-01T08:00:00", "admin", "2026-05-10T08:00:00", "admin");
            return ApiResponse.success(user, "User retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving user", e);
            return ApiResponse.internalError("Error retrieving user");
        }
    }

    public ApiResponse<UserDetails> createUser(UserDetails userDetails) {
        try {
            if (userDetails.getUserName() == null || userDetails.getUserName().isEmpty()) {
                return ApiResponse.badRequest("Username is required");
            }
            userDetails.setId("USER_" + System.currentTimeMillis());
            kafkaProducer.send("user-created", userDetails);
            return ApiResponse.created(userDetails, "User created successfully");
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ApiResponse.internalError("Error creating user");
        }
    }

    public ApiResponse<UserDetails> updateUserStatus(String id, String status) {
        try {
            if (id == null || id.isEmpty()) {
                return ApiResponse.badRequest("User id is required");
            }
            UserDetails user = new UserDetails(id, "John Doe", "jdoe", "Procurement_officer", "jdoe@example.com",
                    "+1234567890", "123 Main St", null, status, "INTERNAL", 0,
                    null, "2026-05-01T08:00:00", "admin", "2026-05-10T08:00:00", "admin");
            return ApiResponse.success(user, "User status updated successfully");
        } catch (Exception e) {
            logger.error("Error updating user status", e);
            return ApiResponse.internalError("Error updating user status");
        }
    }
