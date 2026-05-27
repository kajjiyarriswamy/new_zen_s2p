package com.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gateway.dto.ApiResponse;
import com.gateway.dto.LoginRequest;
import com.gateway.dto.LoginResponse;
import com.gateway.entity.User;
import com.gateway.repo.UserRepository;
import com.gateway.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        
        logger.info("Login attempt for username: {}", request.getUsername());

        try {
            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                logger.warn("Login failed: Username is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.badRequest("Username is required"));
            }

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!request.getPassword().equals(user.getPassword())) {
                logger.warn("Login failed: Invalid password for user {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.unauthorized("Invalid password"));
            }

            String token = jwtService.generateToken(user.getUsername(), user.getRole());
            
            LoginResponse loginResponse = new LoginResponse(
                    token,
                    user.getUsername(),
                    user.getRole(),
                    3600000L  // 1 hour in milliseconds
            );

            logger.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(ApiResponse.success(loginResponse, "Login successful"));

        } catch (RuntimeException e) {
            logger.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized(e.getMessage()));
        } catch (Exception e) {
            logger.error("Internal error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Internal server error"));
        }
    }
}

