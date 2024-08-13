package com.devcci.devtoy.member.application.validator;

import com.devcci.devtoy.member.domain.member.Member;

public interface SignUpValidator {
    void validate(Member member);
}
