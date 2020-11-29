package com.code.to.learn.api.jwt;

import com.code.to.learn.api.model.user.UserRolesTokenModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

public final class JwtUtil {

    public static final int ONE_DAY = 1000 * 60 * 60 * 24;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "learn-to-code-super-secret-key";

    public static String generateToken(UserDetails userDetails, UserRolesTokenModel userRolesTokenModel) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(userRolesTokenModel.getRoleAttribute(), userRolesTokenModel.getParsedRoles());
        return createToken(claims, userDetails.getUsername());
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Optional<String> extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static <T> Optional<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = extractAllClaims(token);
            return Optional.of(claimsResolver.apply(claims));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token).orElse("");
            return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    private static boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).orElse(new Date(Instant.now().getEpochSecond()));
    }

}
