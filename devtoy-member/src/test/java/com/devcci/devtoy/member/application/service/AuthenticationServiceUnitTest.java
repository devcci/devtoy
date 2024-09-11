package com.devcci.devtoy.member.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.AuthenticationException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.infra.redis.RedisKey;
import com.devcci.devtoy.member.application.dto.LoginRequest;
import com.devcci.devtoy.member.application.dto.LoginResponse;
import com.devcci.devtoy.member.application.dto.SignUpRequest;
import com.devcci.devtoy.member.application.validator.SignUpValidator;
import com.devcci.devtoy.member.config.UnitTest;
import com.devcci.devtoy.member.domain.member.Member;
import com.devcci.devtoy.member.domain.member.MemberRepository;
import com.devcci.devtoy.member.domain.member.MemberRole;
import com.devcci.devtoy.member.infra.jwt.JwtProvider;
import com.devcci.devtoy.member.infra.jwt.event.JwtAdditionEvent;
import com.devcci.devtoy.member.infra.jwt.event.JwtDeletionEvent;
import com.devcci.devtoy.member.infra.redis.JwtRedisTemplateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@UnitTest
class AuthenticationServiceUnitTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private JwtRedisTemplateService jwtRedisTemplateService;

    @Mock
    private List<SignUpValidator> signUpValidators;

    @Nested
    @DisplayName("회원가입")
    class SignUpTests {

        @DisplayName("회원가입 성공")
        @Test
        void signUp() {
            // given
            SignUpRequest signUpRequest = new SignUpRequest("testMember", "password", "Test User", "test@example.com",
                Set.of(MemberRole.MEMBER.getValue()));
            String encodedPassword = "encodedPassword";
            given(passwordEncoder.encode(signUpRequest.password())).willReturn(encodedPassword);
            Member member = mock(Member.class);
            given(memberRepository.save(any(Member.class))).willReturn(member);
            signUpValidators.forEach(validator -> doNothing().when(validator).validate(any(Member.class)));

            // when
            Member result = authenticationService.signUp(signUpRequest);

            // then
            assertThat(result).isNotNull();
            verify(memberRepository).save(any(Member.class));
        }
    }

    @Nested
    @DisplayName("로그인")
    class LoginTests {

        @DisplayName("로그인 성공")
        @Test
        void login() {
            // given
            LoginRequest loginRequest = new LoginRequest("testMember", "password");
            Member member = mock(Member.class);
            given(memberRepository.findByMemberId(loginRequest.memberId())).willReturn(Optional.of(member));
            given(passwordEncoder.matches(loginRequest.password(), member.getPassword())).willReturn(true);
            given(jwtProvider.generateAccessToken(member.getMemberId(), member.getRole())).willReturn("accessToken");
            given(jwtProvider.generateRefreshAccessToken(member.getMemberId(), member.getRole())).willReturn(
                "refreshToken");

            // when
            LoginResponse loginResponse = authenticationService.login(loginRequest);

            // then
            assertThat(loginResponse).isNotNull();
            verify(eventPublisher).publishEvent(any(JwtAdditionEvent.class));
        }

        @DisplayName("로그인 실패 - 비밀번호 불일치")
        @Test
        void login_whenPasswordDoesNotMatch_shouldThrowApiException() {
            // given
            LoginRequest loginRequest = new LoginRequest("testMember", "wrongPassword");
            Member member = mock(Member.class);
            given(memberRepository.findByMemberId(loginRequest.memberId())).willReturn(Optional.of(member));
            given(passwordEncoder.matches(loginRequest.password(), member.getPassword())).willReturn(false);

            // when, then
            assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.MEMBER_PASSWORD_INCORRECT.getMessage());

            verify(eventPublisher).publishEvent(any(JwtDeletionEvent.class));
        }

        @DisplayName("로그인 실패 - 회원 없음")
        @Test
        void login_whenMemberNotFound_shouldThrowApiException() {
            // given
            LoginRequest loginRequest = new LoginRequest("testMember", "password");
            given(memberRepository.findByMemberId(loginRequest.memberId())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("로그아웃")
    class LogoutTests {

        @DisplayName("로그아웃 성공")
        @Test
        void logout() {
            // given
            String memberId = "testMember";

            // when
            authenticationService.logout(memberId);

            // then
            verify(eventPublisher).publishEvent(any(JwtDeletionEvent.class));
        }
    }

    @Nested
    @DisplayName("토큰 리프레시")
    class TokenRefreshTests {

        @DisplayName("토큰 리프레시 성공")
        @Test
        void refreshAccessToken() {
            // given
            String memberId = "testMember";
            String refreshToken = "refreshToken";
            String key = RedisKey.REFRESH_TOKEN.generate(memberId);
            given(jwtRedisTemplateService.get(key)).willReturn(refreshToken);
            Member member = mock(Member.class);
            given(memberRepository.findByMemberId(memberId)).willReturn(Optional.of(member));

            // when
            authenticationService.refreshAccessToken(memberId);

            // then
            verify(jwtProvider).generateAccessToken(member.getMemberId(), member.getRole());
        }

        @DisplayName("토큰 리프레시 실패 - Redis에 토큰 없음")
        @Test
        void refreshAccessToken_whenTokenNotInRedis_shouldThrowAuthenticationException() {
            // given
            String memberId = "testMember";
            String key = RedisKey.REFRESH_TOKEN.generate(memberId);
            given(jwtRedisTemplateService.get(key)).willReturn(null);

            // when, then
            assertThatThrownBy(() -> authenticationService.refreshAccessToken(memberId))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining(ErrorCode.JWT_TOKEN_EXPIRED.getMessage());
        }

        @DisplayName("토큰 리프레시 실패 - 회원 없음")
        @Test
        void refreshAccessToken_whenMemberNotFound_shouldThrowApiException() {
            // given
            String memberId = "testMember";
            String refreshToken = "refreshToken";
            String key = RedisKey.REFRESH_TOKEN.generate(memberId);
            given(jwtRedisTemplateService.get(key)).willReturn(refreshToken);
            given(memberRepository.findByMemberId(memberId)).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> authenticationService.refreshAccessToken(memberId))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getMessage());
        }
    }
}
