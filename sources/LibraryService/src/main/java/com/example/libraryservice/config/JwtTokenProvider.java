package com.example.libraryservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor

//This class contains methods for resolving token from request and validating it. Those methods are used in JwtTokenFilter
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.header}")
    private String authorizationHeader;


    public boolean validateToken(String token) {
        System.out.println("validateToken is called");
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        System.out.println("getAuthentication called");
        UserDetails userDetails = new UserDetailsImpl();
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String resolveTokenFromRequest(HttpServletRequest request) {
        System.out.println("resolveTokenFromRequest is called");
        String bearer = request.getHeader(authorizationHeader);
        System.out.println("Bearer: " + bearer);
        if (bearer != null  && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
