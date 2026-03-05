package com.cbn.admincbn.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbn.admincbn.model.User;
import com.cbn.admincbn.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        User newUser = new User();
        newUser.setUsername(username);

        String encodedPassword = passwordEncoder.encode(password);
        
        newUser.setPassword(encodedPassword);
        
        userRepository.save(newUser);
        return "Akun " + username + " berhasil dibuat!";
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userData = userRepository.findByUsername(username);

        if (userData.isPresent()) {
            if (passwordEncoder.matches(password, userData.get().getPassword())) {
                return "Login Berhasil! Selamat Datang " + username;
            }
        }
        return "Login Gagal! Username atau password salah.";
    }
}