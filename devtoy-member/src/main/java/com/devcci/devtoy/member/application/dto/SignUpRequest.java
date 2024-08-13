package com.devcci.devtoy.member.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record SignUpRequest(
    @NotBlank(message = "아이디를 입력해주세요.")
    String memberId,
    @NotBlank(message = "패스워드를 입력해주세요.")
    String password,
    @NotBlank(message = "사용자 이름을 입력해주세요.")
    String name,
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    String email,
    Set<String> role
) {
}
