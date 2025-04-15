package com.sp.web.user.auth.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class UserEntity implements UserDetails {

    private String userId;
    private String userPassword;
    private String nickname;
    private String gender;
    private String birthDate;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // role을 기반으로 권한을 부여
        return (Collection<? extends GrantedAuthority>) Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
