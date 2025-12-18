package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.model.AuthProvider;
import com.example.demo.model.User;
import com.example.demo.model.Role;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

  
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    
    @Transactional
     public String registerUser(User user, boolean isFileUploaded) {
        logger.info("Register request for email={}", user.getEmail());

       
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.info("Email already exists: {}", user.getEmail());
            return null;
        }

        if(user.getProvider() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRole(isFileUploaded ? Role.PRIEST : Role.USER);
        try {
            User saved = userRepository.save(user);
            long idLong = saved.getId() != null ? saved.getId() : 0L;
            return jwtUtil.generateToken(saved.getEmail(), idLong);
        } catch (DataIntegrityViolationException dive) {
          
            logger.warn("DataIntegrityViolation on save (probably race): {}", user.getEmail(), dive);
            return null;
        }
     }

 
    public String loginUser(String email, String password) {
        String normalizedEmail = email == null ? null : email.trim().toLowerCase();
        String rawPassword = password == null ? "" : password;
        if (normalizedEmail == null || normalizedEmail.isEmpty()) return null;

        Optional<User> opt = userRepository.findByEmail(normalizedEmail);
        if (opt.isEmpty()) return null;
        User user = opt.get();

        String storedPassword = user.getPassword();
        if (storedPassword == null) return null;

        boolean ok;
        // Backward-compatibility: if old accounts stored plaintext passwords, allow login once and upgrade to BCrypt.
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            ok = passwordEncoder.matches(rawPassword, storedPassword);
        } else {
            ok = storedPassword.equals(rawPassword);
            if (ok) {
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
            }
        }
        if (!ok) return null;

       
        long idLong = 0L;
        Object idObj = user.getId();
        if (idObj instanceof Number) {
            idLong = ((Number) idObj).longValue();
        } else {
           
            idLong = 0L;
        }

        return jwtUtil.generateToken(user.getEmail(), idLong);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("new_password_required");
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("user_not_found"));
        if (user.getProvider() == AuthProvider.GOOGLE) {
            throw new IllegalArgumentException("oauth_user_no_password");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(oldPassword == null ? "" : oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("invalid_old_password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
