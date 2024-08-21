package com.devcci.devtoy.member.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.AuthenticationException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.util.TokenUtils;
import com.devcci.devtoy.member.application.dto.LoginRequest;
import com.devcci.devtoy.member.application.dto.LoginResponse;
import com.devcci.devtoy.member.application.dto.SignUpRequest;
import com.devcci.devtoy.member.application.validator.SignUpValidator;
import com.devcci.devtoy.member.domain.member.Member;
import com.devcci.devtoy.member.domain.member.MemberRepository;
import com.devcci.devtoy.member.domain.member.MemberRole;
import com.devcci.devtoy.member.infra.cache.redis.JwtRedisRepository;
import com.devcci.devtoy.member.infra.jwt.JwtProvider;
import com.devcci.devtoy.member.infra.jwt.event.JwtAdditionEvent;
import com.devcci.devtoy.member.infra.jwt.event.JwtDeletionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final List<SignUpValidator> signUpValidators;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtRedisRepository jwtRedisRepository;

    public AuthenticationService(
        MemberRepository memberRepository,
        PasswordEncoder passwordEncoder,
        JwtProvider jwtProvider,
        List<SignUpValidator> signUpValidators,
        ApplicationEventPublisher eventPublisher,
        JwtRedisRepository jwtRedisRepository
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.signUpValidators = signUpValidators;
        this.eventPublisher = eventPublisher;
        this.jwtRedisRepository = jwtRedisRepository;
    }

    @Transactional
    public Member signUp(SignUpRequest request) {
        String encodePassword = passwordEncoder.encode(request.password());
        Set<MemberRole> collect = request.role().stream().map(x -> MemberRole.valueOf(x.toUpperCase())).collect(Collectors.toSet());
        Member signUpMember = Member.createMember(
            request.memberId(), encodePassword, request.name(), request.email(), collect);
        signUpValidators.forEach(validator -> validator.validate(signUpMember));
        return memberRepository.save(signUpMember);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId())
            .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (passwordEncoder.matches(request.password(), member.getPassword())) {
            LoginResponse response = LoginResponse.of(member.getName(), member.getRole(),
                jwtProvider.generateAccessToken(member.getMemberId(), member.getRole())
            );
            String refreshToken = jwtProvider.generateRefreshAccessToken(member.getMemberId(), member.getRole());
            eventPublisher.publishEvent(new JwtAdditionEvent(member.getMemberId(), response.getAccessToken(), refreshToken));
            return response;
        } else {
            eventPublisher.publishEvent(new JwtDeletionEvent(member.getMemberId()));
            throw new ApiException(ErrorCode.MEMBER_PASSWORD_INCORRECT);
        }
    }

    @Transactional
    public void logout(String auth) {
        String token = TokenUtils.extractBearerToken(auth);
        String memberId = jwtProvider.getMemberId(token);
        eventPublisher.publishEvent(new JwtDeletionEvent(memberId));
    }

    public String refreshAccessToken(String auth) {
        String token = TokenUtils.extractBearerToken(auth);
        String memberId = jwtProvider.getMemberId(token);
        if (!jwtRedisRepository.existsById(memberId)) {
            throw new AuthenticationException(ErrorCode.JWT_TOKEN_EXPIRED);
        }
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));
        return jwtProvider.generateAccessToken(member.getMemberId(), member.getRole());
    }
}
