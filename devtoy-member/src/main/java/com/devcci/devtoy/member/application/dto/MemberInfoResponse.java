package com.devcci.devtoy.member.application.dto;

import com.devcci.devtoy.member.domain.member.Member;
import com.devcci.devtoy.member.domain.member.MemberRole;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MemberInfoResponse {
    private Long id;
    private String memberId;
    private String name;
    private String email;
    private Set<String> roles;

    private MemberInfoResponse(Long id, String memberId, String name, String email, Set<String> roles) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
            member.getId(),
            member.getMemberId(),
            member.getName(),
            member.getEmail(),
            member.getRole().stream().map(MemberRole::getValue).collect(Collectors.toSet())
        );
    }

    public static List<MemberInfoResponse> of(List<Member> members) {
        return members.stream().map(MemberInfoResponse::of).toList();
    }
}
