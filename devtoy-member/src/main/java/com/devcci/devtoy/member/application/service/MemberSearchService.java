package com.devcci.devtoy.member.application.service;

import com.devcci.devtoy.member.application.dto.MemberInfoResponse;
import com.devcci.devtoy.member.domain.member.Member;
import com.devcci.devtoy.member.domain.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberSearchService {
    private final MemberRepository memberRepository;

    public MemberSearchService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberInfoResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return MemberInfoResponse.of(members);
    }
}
