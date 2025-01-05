package notesmaking.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // Generate JWT Token
    public static String generateToken(String userId, String email) {
        return Jwts.builder()
                .setSubject(userId) // User ID as the subject
                .claim("email", email) // Add email as a claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY) // Sign with SecretKey
                .compact();
    }

    // Validate JWT Token
    public static Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return null;
        }
    }

    // Extract User ID from Token
    public static String extractUserId(String token) {
        Claims claims = validateToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    // Extract User Email from Token
    public static String extractUserEmail(String token) {
        Claims claims = validateToken(token);
        return claims != null ? claims.get("email", String.class) : null;
    }


}
