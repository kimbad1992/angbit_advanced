package com.angbit.angbit_advanced.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "MEMBER_ASSET")
public class MemberAsset {

    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "KRW", nullable = false)
    private Integer krw = 0;

    @Column(name = "ASSET", nullable = false)
    private Integer asset = 30000000;

    public MemberAsset() {
    }

    public MemberAsset(Member member) {
        this.member = member;
        this.memberId = member.getId();
    }
}
