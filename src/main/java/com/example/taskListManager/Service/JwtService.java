package com.example.taskListManager.Service;

import com.example.taskListManager.Model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getUserEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 2000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            if (username == null) {
                System.out.println("Username не найден в токене");
                return false;
            }
            if (!username.equals(userDetails.getUsername())) {
                System.out.println("Username из токена не совпадает с UserDetails");
                return false;
            }
            if (isTokenExpired(token)) {
                System.out.println("Токен истек");
                return false;
            }
            return true;
            //return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (ExpiredJwtException e) {
            System.out.println("JWT истёк: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("JWT повреждён: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT неподдерживаемый: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка валидации JWT: " + e.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}