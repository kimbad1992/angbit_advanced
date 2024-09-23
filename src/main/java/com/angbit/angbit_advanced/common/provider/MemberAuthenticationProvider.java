package com.angbit.angbit_advanced.common.provider;

import com.angbit.angbit_advanced.entity.Member;
import com.angbit.angbit_advanced.entity.MemberDetails;
import com.angbit.angbit_advanced.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MemberAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private MemberService memberService;

    public MemberAuthenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        MemberDetails memberDetail = (MemberDetails) memberService.loadUserByUsername(username);
        String dbPassword = memberDetail.getPassword(); // 이미 암호화된 값

        log.info("Auth");

        if (!getPasswordEncoder().matches(password, dbPassword)) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        Member user = memberDetail.getMember();

        if (user == null || !user.getEnabled()) {
            throw new BadCredentialsException("사용할 수 없는 계정입니다.");
        }

        return new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
