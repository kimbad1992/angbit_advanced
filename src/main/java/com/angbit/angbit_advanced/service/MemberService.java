package com.angbit.angbit_advanced.service;

import com.angbit.angbit_advanced.entity.Member;
import com.angbit.angbit_advanced.entity.MemberDetails;
import com.angbit.angbit_advanced.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
@Slf4j
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
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
}

