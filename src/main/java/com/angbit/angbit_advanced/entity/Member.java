package com.angbit.angbit_advanced.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Entity
@Table(name = "MEMBER")
public class Member implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id; // 회원 고유 번호

    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username; // 아이디

    @Column(name = "PASSWORD", nullable = false)
    private String password; // 비밀번호

    @Column(name = "NICKNAME", nullable = false, length = 50)
    private String nickname; // 닉네임

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email; // 이메일

    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled = true; // 계정 활성화 여부

    @Column(name = "JOINDATE", nullable = false)
    private Timestamp joinDate; // 가입일

    @Column(name = "FINALDATE", nullable = false)
    private Timestamp finalDate; // 최종수정일

    // 연관 관계 매핑
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberAsset asset;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberOAuth oauth;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Member2FA twoFactor;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberRole> roles;

    public Member() {
        this.joinDate = new Timestamp(System.currentTimeMillis());
        this.finalDate = new Timestamp(System.currentTimeMillis());
    }
}
