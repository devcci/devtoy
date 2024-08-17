package com.devcci.devtoy.member.domain.member;

import com.devcci.devtoy.common.domain.BaseTimeEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member",
    uniqueConstraints = {
        @UniqueConstraint(name = "member_id_uq", columnNames = "memberId")
    }
)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @Comment("암호화")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "member_role",
        joinColumns = {@JoinColumn(name = "id")})
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> role;

    private Member(String memberId, String password, String name, String email, Set<MemberRole> role) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }


    public static Member createMember(String memberId, String password, String name, String email, Set<MemberRole> role) {
        return new Member(memberId, password, name, email, role);
    }
}
