package com.angbit.angbit_advanced.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class MemberDetails implements UserDetails {

    private final Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 필요한 경우 추가 구현
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 필요한 경우 추가 구현
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 필요한 경우 추가 구현
    }

    @Override
    public boolean isEnabled() {
        return member.getEnabled();
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getNickname() {
        return member.getNickname();
    }

    public Integer getKrw() {
        return member.getAsset().getKrw();
    }

    public Integer getAsset() {
        return member.getAsset().getAsset();
    }

    public String getOauthProvider() {
        return member.getOauth().getOauthProvider();
    }

    public String getOauthProviderId() {
        return member.getOauth().getOauthProviderId();
    }

    public String getOauthProfilePicture() {
        return member.getOauth().getOauthProfilePicture();
    }

    public Boolean getTwoFactorEnabled() {
        return member.getTwoFactor().getTwoFactorEnabled();
    }

    public String getTwoFactorSecret() {
        return member.getTwoFactor().getTwoFactorSecret();
    }
}
