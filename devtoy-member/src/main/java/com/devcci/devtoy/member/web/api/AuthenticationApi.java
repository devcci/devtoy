package com.devcci.devtoy.member.web.api;

import com.devcci.devtoy.member.application.dto.LoginRequest;
import com.devcci.devtoy.member.application.dto.LoginResponse;
import com.devcci.devtoy.member.application.dto.SignUpRequest;
import com.devcci.devtoy.member.application.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    public AuthenticationApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<Void> memberSignUp(@Valid @RequestBody SignUpRequest request) {
        authenticationService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> memberLogin(@Valid @RequestBody LoginRequest request) {
        LoginResponse login = authenticationService.login(request);
        return ResponseEntity.ok().body(login);
    }
}
