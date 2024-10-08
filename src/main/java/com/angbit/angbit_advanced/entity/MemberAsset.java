package com.angbit.angbit_advanced.entity;

import com.angbit.angbit_advanced.common.constant.Constants;
import jakarta.persistence.*;
import lombok.Builder;
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
    private Integer krw = Constants.INTEGER_ZERO;

    @Column(name = "ASSET", nullable = false)
    private Integer asset = Constants.MEMBER_DEFAULT_ASSET;

    public MemberAsset() {
    }

    public MemberAsset(Member member) {
        this.member = member;
        this.memberId = member.getId();
    }

    @Builder
    public MemberAsset(Member member, Integer krw, Integer asset) {
        this.member = member;
        this.memberId = member.getId();
        this.krw = krw != null ? krw : Constants.INTEGER_ZERO;
        this.asset = asset != null ? asset : Constants.MEMBER_DEFAULT_ASSET;
    }
}
