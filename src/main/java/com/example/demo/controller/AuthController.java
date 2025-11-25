package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.controller.RegisterRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(registerRequest.getPassword());
            user.setName(registerRequest.getName());
            user.setPhone(registerRequest.getPhone());

            String token = authService.registerUser(user, registerRequest.isFileUploaded());
            if (token == null) {
                return ResponseEntity.badRequest().body("email_exists");
            }
          
            return ResponseEntity.ok(token);
        } catch (IllegalStateException ise) {
           
            logger.error("Auth/register configuration error: {}", ise.getMessage(), ise);
            return ResponseEntity.status(500).body("server_configuration_error: " + ise.getMessage());
        } catch (Exception ex) {
            logger.error("Register failed", ex);
            return ResponseEntity.status(500).body("internal_error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String token = authService.loginUser(user.getEmail(), user.getPassword());
            if (token == null) return ResponseEntity.status(401).body("invalid_credentials");
            return ResponseEntity.ok(token);
        } catch (IllegalStateException ise) {
          
            logger.error("Auth error: {}", ise.getMessage(), ise);
            return ResponseEntity.status(500).body("server_configuration_error: " + ise.getMessage());
        } catch (Exception ex) {
            logger.error("Login failed", ex);
            return ResponseEntity.status(500).body("internal_error");
        }
    }

    
    @GetMapping("/google-url")
    public String googleUrl() {
        return "/oauth2/authorization/google";
    }
}