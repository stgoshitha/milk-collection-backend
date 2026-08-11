package com.project.milkcollection.security;

import com.project.milkcollection.common.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    // Generate access token
    public String generateAccessToken(
            UUID userId,
            String username,
            String role
    ) {

        return generateToken(
                userId,
                username,
                role,
                jwtProperties.getExpiration(),
                SecurityConstants.ACCESS_TOKEN
        );
    }

    // Generate refresh token
    public String generateRefreshToken(UUID userId) {

        return Jwts.builder()
                .subject(userId.toString())
                .claim(
                        SecurityConstants.CLAIM_TOKEN_TYPE,
                        SecurityConstants.REFRESH_TOKEN
                )
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtProperties
                                        .getRefreshTokenExpiration()
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // Common access-token generator
    private String generateToken(
            UUID userId,
            String username,
            String role,
            long expiration,
            String tokenType
    ) {

        return Jwts.builder()
                .subject(userId.toString())
                .claims(
                        Map.of(
                                SecurityConstants.CLAIM_USERNAME,
                                username,

                                SecurityConstants.CLAIM_ROLE,
                                role,

                                SecurityConstants.CLAIM_TOKEN_TYPE,
                                tokenType
                        )
                )
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // Extract user ID
    public UUID extractUserId(String token) {

        return extractClaim(
                token,
                claims -> UUID.fromString(
                        claims.getSubject()
                )
        );
    }

    // Extract username
    public String extractUsername(String token) {

        return extractClaim(
                token,
                claims -> claims.get(
                        SecurityConstants.CLAIM_USERNAME,
                        String.class
                )
        );
    }

    // Extract role
    public String extractRole(String token) {

        return extractClaim(
                token,
                claims -> claims.get(
                        SecurityConstants.CLAIM_ROLE,
                        String.class
                )
        );
    }

    // Extract token type
    public String extractTokenType(String token) {

        return extractClaim(
                token,
                claims -> claims.get(
                        SecurityConstants.CLAIM_TOKEN_TYPE,
                        String.class
                )
        );
    }

    // Validate access token
    public boolean isTokenValid(
            String token,
            UUID userId
    ) {

        try {

            return userId.equals(extractUserId(token))
                    && SecurityConstants.ACCESS_TOKEN.equals(
                    extractTokenType(token)
            )
                    && !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException exception) {

            return false;
        }
    }

    // Validate refresh token
    public boolean isRefreshTokenValid(String token) {

        try {

            return SecurityConstants.REFRESH_TOKEN.equals(
                    extractTokenType(token)
            )
                    && !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException exception) {

            return false;
        }
    }

    // Extract any claim
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    // Check token expiration
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    // Extract expiration
    private Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    // Parse and verify JWT
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // JWT secret key
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties
                        .getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}