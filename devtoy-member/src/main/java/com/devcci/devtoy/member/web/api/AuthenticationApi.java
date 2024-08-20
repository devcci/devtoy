package com.devcci.devtoy.member.web.api;

import com.devcci.devtoy.member.application.dto.LoginRequest;
import com.devcci.devtoy.member.application.dto.LoginResponse;
import com.devcci.devtoy.member.application.dto.SignUpRequest;
import com.devcci.devtoy.member.application.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/auth")
@RestController
public class AuthenticationApi {
    private final AuthenticationService authenticationService;

    public AuthenticationApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("signup")
    public ResponseEntity<Void> memberSignUp(@Valid @RequestBody SignUpRequest request) {
        authenticationService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> memberLogin(@Valid @RequestBody LoginRequest request) {
        LoginResponse login = authenticationService.login(request);
        return ResponseEntity.ok().body(login);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> memberLogout(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        authenticationService.logout(auth);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String accessToken = authenticationService.refreshAccessToken(auth);
        return ResponseEntity.ok()
            .body(Map.of("accessToken", accessToken));
    }

}
