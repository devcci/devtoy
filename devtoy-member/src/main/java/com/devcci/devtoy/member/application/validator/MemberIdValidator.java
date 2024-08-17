package com.devcci.devtoy.member.application.validator;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.member.domain.member.Member;
import com.devcci.devtoy.member.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberIdValidator implements SignUpValidator {
    private final MemberRepository memberRepository;

    public MemberIdValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void validate(Member member) {
        if (memberRepository.existsByMemberId(member.getMemberId())) {
            throw new ApiException(ErrorCode.MEMBER_ID_ALREADY_EXISTS);
        }
    }
}
