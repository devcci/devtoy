package com.devcci.devtoy.member.infra.jwt.event;

public record JwtDeletionEvent(
    String memberId
) {
}