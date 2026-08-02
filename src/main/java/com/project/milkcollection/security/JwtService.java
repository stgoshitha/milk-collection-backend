package com.project.milkcollection.security;

import com.project.milkcollection.common.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    //Generate Access Token
    public String generateAccessToken(
            String userId,
            String username,
            String role
    ) {
        return generateToken(
                userId,
                username,
                role,
                jwtProperties.getExpiration()
        );
    }

    //Generate Refresh Token
    public String generateRefreshToken(String userId){
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtProperties.getRefreshTokenExpiration()
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // Common JWT Generator
    private String generateToken(
            String userId,
            String username,
            String role,
            long expiration
    ) {

        return Jwts.builder()
                // Primary identifier
                .subject(userId)
                // Extra claims
                .claims(
                        Map.of(
                                SecurityConstants.CLAIM_USERNAME, username,
                                SecurityConstants.CLAIM_ROLE, role
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


    //Extract User ID
    public String extractUserId(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }


    //Extract Username
    public String extractUsername(
            String token
    ) {

        return extractClaim(
                token,
                claims -> claims.get(
                        SecurityConstants.CLAIM_USERNAME,
                        String.class
                )
        );
    }


    //Extract Role
    public String extractRole(
            String token
    ) {

        return extractClaim(
                token,
                claims -> claims.get(
                        SecurityConstants.CLAIM_ROLE,
                        String.class
                )
        );
    }


    //Validate Token
    public boolean isTokenValid(
            String token,
            String userId
    ) {

        return userId.equals(extractUserId(token))
                && !isTokenExpired(token);
    }


    //Extract Any Claim
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims =
                extractAllClaims(token);

        return resolver.apply(claims);
    }


    //Check Expiration
    private boolean isTokenExpired(
            String token
    ) {

        return extractExpiration(token)
                .before(new Date());
    }


    //Extract Expiration
    private Date extractExpiration(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }


    //Parse JWT
    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parser()

                .verifyWith(
                        getSigningKey()
                )

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }


    //JWT Secret Key
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties
                        .getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}