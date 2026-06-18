package com.recordedapp.recordedback.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secretKey}")
    private String base64Secret;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    /**
     * Decode the Base64-encoded secret key and return a proper SecretKey instance.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate a JWT token for the given user.
     *
     * @param userDetails the authenticated user's details
     * @return the generated JWT token as a String
     */
    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();

        if (username == null || username.trim().isEmpty()) {
            logger.error("Cannot generate token: Username is null or empty.");
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract the username from the token.
     *
     * @param token JWT token
     * @return username (subject)
     */
    public String extractUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (Exception e) {
            logger.error("Failed to extract username from token.", e);
            throw new RuntimeException("Invalid token", e);
        }
    }

    /**
     * Validate the JWT token by checking username and expiration.
     *
     * @param token        JWT token
     * @param userDetails  user info to compare with token claims
     * @return true if valid, false if invalid
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);

            if (!username.equals(userDetails.getUsername())) {
                logger.warn("JWT validation failed: Username mismatch. Token username: {}, Expected: {}",
                        username, userDetails.getUsername());
                return false;
            }

            if (isTokenExpired(token)) {
                logger.warn("JWT validation failed: Token is expired.");
                return false;
            }

            return true;

        } catch (ExpiredJwtException ex) {
            logger.error("JWT validation failed: Token has expired", ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("JWT validation failed: Unsupported token", ex);
        } catch (MalformedJwtException ex) {
            logger.error("JWT validation failed: Malformed token", ex);
        } catch (SecurityException ex) {
            logger.error("JWT validation failed: Invalid signature", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("JWT validation failed: Token string is null or empty", ex);
        } catch (Exception ex) {
            logger.error("JWT validation failed: Unexpected error", ex);
        }

        return false;
    }

    /**
     * Check whether the token has expired.
     *
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        try {
            Date expirationDate = getClaims(token).getExpiration();
            return expirationDate.before(new Date());
        } catch (Exception e) {
            logger.error("Could not determine token expiration.", e);
            return true; // If we can't tell, we assume it's expired.
        }
    }

    /**
     * Parses the JWT token and returns its claims.
     *
     * @param token the JWT token string
     * @return the claims (payload) extracted from the token
     */
    private Claims getClaims(String token) {
        // Step 1: Create a JwtParserBuilder with the signing key
        JwtParserBuilder parserBuilder = Jwts.parserBuilder();

        // Step 2: Set the signing key used to verify the token's signature
        parserBuilder.setSigningKey(getSigningKey());

        // Step 3: Build the parser
        JwtParser jwtParser = parserBuilder.build();

        // Step 4: Parse the token and get the Jws (signed JWT)
        Jws<Claims> jws = jwtParser.parseClaimsJws(token);

        // Step 5: Extract the body, which contains the claims
        Claims claims = jws.getBody();

        // Step 6: Return the claims
        return claims;
    }

}