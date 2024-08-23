package com.devcci.devtoy.member.infra.jwt.event;

public record JwtAdditionEvent(
    String memberId,
    String refreshToken) {
}