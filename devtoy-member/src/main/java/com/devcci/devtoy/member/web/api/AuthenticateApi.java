package com.devcci.devtoy.member.web.api;

import com.devcci.devtoy.member.application.service.AuthenticateService;
import com.devcci.devtoy.member.web.dto.LoginRequest;
import com.devcci.devtoy.member.web.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticateApi {
    private final AuthenticateService authenticateService;

    public AuthenticateApi(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping
    public ResponseEntity<Void> memberSignUp(SignUpRequest request) {
        authenticateService.signUpMember(request);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> memberLogin(LoginRequest request) {
        return ResponseEntity.ok().build();
    }
}
