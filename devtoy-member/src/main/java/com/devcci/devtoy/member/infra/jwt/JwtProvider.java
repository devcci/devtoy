package com.devcci.devtoy.member.infra.jwt;

import com.devcci.devtoy.member.domain.member.MemberRole;
import com.devcci.devtoy.member.infra.jwt.auth.AuthConstants;
import com.devcci.devtoy.member.infra.jwt.auth.MemberDetailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final MemberDetailService memberDetailService;

    public JwtProvider(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.expire-time-hour}") Long expireTimeHour,
        @Value("${jwt.refresh-expire-time-hour}") Long refreshExpireTimeHour,
        MemberDetailService memberDetailService
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expireTimeHour = expireTimeHour;
        this.refreshExpireTimeHour = refreshExpireTimeHour;
        this.memberDetailService = memberDetailService;
    }

    public String getMemberId(String token) {

        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = memberDetailService.loadUserByUsername(this.getMemberId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "Authorization", userDetails.getAuthorities());
    }


    public Boolean isExpired(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }

    public String generateAccessToken(String subject, Set<MemberRole> roles) {
        return Jwts.builder()
            .subject(subject)
            .claim(AuthConstants.ROLES, roles)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(expireTimeHour, ChronoUnit.HOURS)))
            .signWith(secretKey)
            .compact();
    }

    public String generateRefreshAccessToken(String subject, Set<MemberRole> roles) {
        return Jwts.builder()
            .subject(subject)
            .claim(AuthConstants.ROLES, roles)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(refreshExpireTimeHour, ChronoUnit.HOURS)))
            .signWith(secretKey)
            .compact();
    }
}
