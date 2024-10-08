package com.angbit.angbit_advanced.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "MEMBER_ROLE")
@IdClass(MemberRoleId.class)
public class MemberRole {

    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Id
    @Column(name = "ROLE")
    private String role;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", insertable = false, updatable = false)
    private Member member;

    public MemberRole() {
    }

    @Builder
    public MemberRole(Member member, String role) {
        this.member = member;
        this.memberId = member.getId();
        this.role = role;
    }
}