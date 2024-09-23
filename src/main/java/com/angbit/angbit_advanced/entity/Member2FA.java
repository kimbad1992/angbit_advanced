package com.angbit.angbit_advanced.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "MEMBER_2FA")
public class Member2FA {

    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "TWO_FACTOR_ENABLED", nullable = false)
    private Boolean twoFactorEnabled = false;

    @Column(name = "TWO_FACTOR_SECRET")
    private String twoFactorSecret;

    public Member2FA() {
    }

    public Member2FA(Member member) {
        this.member = member;
        this.memberId = member.getId();
    }
}
