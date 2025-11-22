package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
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
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return null;
        User user = opt.get();

      
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

       
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
}
