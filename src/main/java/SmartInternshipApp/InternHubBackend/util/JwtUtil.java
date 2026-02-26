package SmartInternshipApp.InternHubBackend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    
    private static final String SECRET = "YourSecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256Algorithm";
    private static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 30 days
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    
    public String generateToken(String email, String userType, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userType", userType)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getEmailFromToken(String token) {
        return validateToken(token).getSubject();
    }
}
