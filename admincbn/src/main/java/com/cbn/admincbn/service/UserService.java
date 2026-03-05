package com.cbn.admincbn.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cbn.admincbn.konfigurasi.JwtUtil;
import com.cbn.admincbn.model.User;
import com.cbn.admincbn.repository.UserRepository;

import jakarta.servlet.http.HttpSession; 

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String generateCaptchaText() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; 
        StringBuilder captcha = new StringBuilder();
        Random rnd = new Random();
        while (captcha.length() < 5) { 
            captcha.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return captcha.toString();
    }

    public Map<String, Object> loginProses(String username, String password, String inputCaptcha, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        String sessionCaptcha = (String) session.getAttribute("captcha");

        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(inputCaptcha)) {
            response.put("status", "error");
            response.put("message", "Login Gagal: Captcha yang Anda masukkan salah!");
            return response;
        }

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            if (passwordEncoder.matches(password, user.getPassword())) { 
                String token = jwtUtil.generateToken(username);
                
                session.removeAttribute("captcha"); 

                response.put("status", "success");
                response.put("message", "Login Berhasil! Selamat datang " + username);
                response.put("token", token);
                return response;
            } else {
                response.put("status", "error");
                response.put("message", "Login Gagal: Password salah!");
                return response;
            }
        } else {
            response.put("status", "error");
            response.put("message", "Login Gagal: Username salah!");
            return response;
        }
    }

    public User save(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}