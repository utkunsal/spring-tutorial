package com.example.controllers;

import com.example.auth.AuthenticationRequest;
import com.example.auth.AuthenticationResponse;
import com.example.services.AuthenticationService;
import com.example.auth.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        AuthenticationResponse authResponse = authenticationService.register(request);
        return ResponseEntity.ok(new UserDTO(authResponse.getName()));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response){
        AuthenticationResponse authResponse = authenticationService.authenticate(request);
        addCookie("access_token", authResponse.getToken(), response);
        addCookie("refresh_token", authResponse.getRefreshToken(), response);
        return ResponseEntity.ok(new UserDTO(authResponse.getName()));
    }

    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        AuthenticationResponse authResponse = authenticationService.refreshToken(request);
        addCookie("access_token", authResponse.getToken(), response);
        addCookie("refresh_token", authResponse.getRefreshToken(), response);
    }

    private void addCookie(String name, String content, HttpServletResponse response){
        Cookie cookie = new Cookie(name, content);
        cookie.setMaxAge(86400000);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    record UserDTO(
            String name
    ){}
}
