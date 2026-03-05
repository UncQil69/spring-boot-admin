package com.cbn.admincbn.controller;

import java.util.Map;
import java.util.HashMap; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbn.admincbn.model.User;
import com.cbn.admincbn.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession; 

@RestController 
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoint untuk mengelola data user")
@SecurityRequirement(name = "Bearer Authentication") 
public class UserController {

    @Autowired
    private UserService userService; 

    @Operation(summary = "Get Captcha", description = "Mendapatkan kode captcha untuk digunakan saat login")
    @GetMapping("/captcha")
    public Map<String, String> getCaptcha(HttpSession session) {
        String captcha = userService.generateCaptchaText();
        session.setAttribute("captcha", captcha); 
        
        Map<String, String> response = new HashMap<>();
        response.put("captcha", captcha);
        response.put("info", "Masukkan kode ini pada parameter captcha saat login");
        return response;
    }

    @Operation(summary = "Login User", description = "Login dengan validasi username, password, dan captcha")
    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestParam String username, 
            @RequestParam String password,
            @RequestParam String captcha, 
            HttpSession session) {
        return userService.loginProses(username, password, captcha, session);
    }

    @Operation(summary = "Tambah user baru", description = "Simpan user baru")
    @PostMapping("/register") 
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
}