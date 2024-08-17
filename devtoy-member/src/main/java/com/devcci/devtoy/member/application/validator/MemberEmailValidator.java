package com.devcci.devtoy.member.application.validator;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.member.domain.member.Member;
import com.devcci.devtoy.member.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberEmailValidator implements SignUpValidator {
    private final MemberRepository memberRepository;

    public MemberEmailValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void validate(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new ApiException(ErrorCode.MEMBER_EMAIL_ALREADY_EXISTS);
        }
    }
}
