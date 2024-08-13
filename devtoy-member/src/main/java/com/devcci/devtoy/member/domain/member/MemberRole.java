package com.devcci.devtoy.member.domain.member;

import lombok.Getter;

@Getter
public enum MemberRole {
    MEMBER("MEMBER"),
    ADMIN("ADMIN"),
    ;

    final String value;

    MemberRole(String value) {
        this.value = value;
    }
}
