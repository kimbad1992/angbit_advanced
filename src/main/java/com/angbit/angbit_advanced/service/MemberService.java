package com.angbit.angbit_advanced.service;

import com.angbit.angbit_advanced.common.constant.RoleEnum;
import com.angbit.angbit_advanced.entity.*;
import com.angbit.angbit_advanced.model.MemberDto;
import com.angbit.angbit_advanced.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

@Service
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("ID를 입력해주세요.");
        }

        log.info("loadUserByUsername: {}", username);

        // Username으로 Member를 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호를 잘못 입력했습니다."));

        if (!member.getEnabled()) {
            throw new UsernameNotFoundException("사용할 수 없는 계정입니다.");
        }

        return new MemberDetails(member);
    }

    @Transactional(rollbackOn = Exception.class)
    public void signup(MemberDto memberDto) throws Exception {
        // TODO : 커스텀 Exception으로 변경하기
        if (memberRepository.existsByUsername(memberDto.getUsername())) {
            throw new Exception("이미 존재하는 아이디입니다.");
        }

        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new Exception("이미 사용중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());

        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(encodedPassword)
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .build();

        // Member ID 채번을 위한 저장
        memberRepository.save(member);

        MemberAsset asset = MemberAsset.builder()
                .member(member)
                .build();

        MemberRole role = MemberRole.builder()
                .member(member)
                .role(RoleEnum.ROLE_USER.value())
                .build();

        // 연관 관계 설정
        member.setAsset(asset);
        member.setRoles(new ArrayList<>(Collections.singletonList(role)));

//        if (using2FA) {
//            Member2FA member2FA = Member2FA.builder()
//                    .member(member)
//                    .twoFactorSecret()
//                    .twoFactorEnabled()
//                    .build();
//            member.setTwoFactor(member2FA);
//        }

//        if (usingOAuth) {
//            MemberOAuth memberOAuth = MemberOAuth.builder()
//                    .member(member)
//                    .oauthProvider()
//                    .oauthProviderId()
//                    .oauthProfilePicture()
//                    .build();
//        }

        // 연관된 Entity도 함께 저장
        memberRepository.save(member);

        log.debug("User Saved : {}", member);
    }
}

