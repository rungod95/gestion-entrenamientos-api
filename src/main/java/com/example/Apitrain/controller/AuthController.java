package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder) {
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        if ("user".equals(username) && "password".equals(password)) { // Aquí conecta con tu base de datos
            String token = jwtTokenService.generateToken(username);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {
        // Guarda username y contraseña hasheada en la base de datos
        String hashedPassword = passwordEncoder.encode(password);
        // Simula guardar en BD
        return ResponseEntity.status(201).body("Usuario registrado");
    }
}
