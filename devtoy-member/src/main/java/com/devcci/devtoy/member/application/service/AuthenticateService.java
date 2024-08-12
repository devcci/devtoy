package com.devcci.devtoy.member.application.service;

import com.devcci.devtoy.member.domain.member.Member;
import com.devcci.devtoy.member.domain.member.MemberRepository;
import com.devcci.devtoy.member.web.dto.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticateService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateService(MemberRepository memberRepository
        , PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Member signUpMember(SignUpRequest request) {
        String encodePassword = passwordEncoder.encode(request.password());
        Member signUpMember = Member.createMember(
            request.memberId(), request.password(), request.name(), request.email());
        return memberRepository.save(signUpMember);
    }
}
