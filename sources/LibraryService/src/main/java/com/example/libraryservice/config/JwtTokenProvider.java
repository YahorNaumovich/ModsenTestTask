package com.example.libraryservice.config;

import com.example.libraryservice.config.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletRequest;

//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private static final String JWT_USERNAME = "username";
    private static final String JWT_ROLES = "roles";

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.expiration}")
    private long validityInMinutes;

//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }

    public boolean validateToken(String token) {
        System.out.println("validateToken is called");
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public String getUsername(String token) {
        return String.valueOf(Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token).getBody().get(JWT_USERNAME));
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

    public String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
