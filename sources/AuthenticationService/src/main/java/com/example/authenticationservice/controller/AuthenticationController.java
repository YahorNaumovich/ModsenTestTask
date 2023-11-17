package com.example.authenticationservice.controller;

import com.example.authenticationservice.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.header}")
    private String authorizationHeader;

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get authentication token")
    public ResponseEntity<Void> generateBearerToken(String username){
        String token = jwtTokenProvider.createToken(username);
        return ResponseEntity.ok().header(authorizationHeader, "Bearer " + token).build();
    }
}
