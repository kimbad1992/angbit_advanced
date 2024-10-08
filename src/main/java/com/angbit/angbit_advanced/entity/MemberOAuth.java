package com.angbit.angbit_advanced.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "MEMBER_OAUTH")
public class MemberOAuth {

    @Id
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "OAUTH_PROVIDER", length = 50)
    private String oauthProvider;

    @Column(name = "OAUTH_PROVIDER_ID", length = 100)
    private String oauthProviderId;

    @Column(name = "OAUTH_PROFILE_PICTURE", length = 255)
    private String oauthProfilePicture;

    public MemberOAuth() {
    }

    @Builder
    public MemberOAuth(Member member, String oauthProvider, String oauthProviderId, String oauthProfilePicture) {
        this.member = member;
        this.memberId = member.getId();
    }
}
