package com.devcci.devtoy.member.web.api;

import com.devcci.devtoy.member.application.dto.MemberInfoResponse;
import com.devcci.devtoy.member.application.service.MemberSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/member")
@RestController
public class MemberSearchApi {
    private final MemberSearchService memberSearchService;

    public MemberSearchApi(MemberSearchService memberSearchService) {
        this.memberSearchService = memberSearchService;
    }

    @GetMapping
    public ResponseEntity<List<MemberInfoResponse>> getAllMembers() {

        return ResponseEntity.ok().body(memberSearchService.getAllMembers());
    }
}
