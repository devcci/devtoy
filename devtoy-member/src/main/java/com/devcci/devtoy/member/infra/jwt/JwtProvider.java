package com.devcci.devtoy.member.infra.jwt;

import com.devcci.devtoy.common.Constans.AuthConstants;
import com.devcci.devtoy.member.domain.member.MemberRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final Long expireTimeHour;
    private final Long refreshExpireTimeHour;

    public JwtProvider(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.expire-time-hour}") Long expireTimeHour,
        @Value("${jwt.refresh-expire-time-hour}") Long refreshExpireTimeHour
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expireTimeHour = expireTimeHour;
        this.refreshExpireTimeHour = refreshExpireTimeHour;
    }


    public String generateAccessToken(String subject, Set<MemberRole> roles) {
        return Jwts.builder()
            .subject(subject)
            .claim(AuthConstants.ROLES, roles)
            .claim(AuthConstants.TYPE, AuthConstants.ACCESS_TOKEN)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(expireTimeHour, ChronoUnit.HOURS)))
            .signWith(secretKey)
            .compact();
    }

    public String generateRefreshAccessToken(String subject, Set<MemberRole> roles) {
        return Jwts.builder()
            .subject(subject)
            .claim(AuthConstants.ROLES, roles)
            .claim(AuthConstants.TYPE, AuthConstants.REFRESH_TOKEN)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(refreshExpireTimeHour, ChronoUnit.HOURS)))
            .signWith(secretKey)
            .compact();
    }

    public String getMemberId(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
