package com.esteban.pagina.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String username;
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;


    private UserPrincipal(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal.UserPrincipalBuilder builder() {
        return new UserPrincipal.UserPrincipalBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public static class UserPrincipalBuilder {
        private Long userId;
        private String username;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;

        private UserPrincipalBuilder() {
        }

        public UserPrincipalBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserPrincipalBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserPrincipalBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserPrincipalBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserPrincipal build() {
            return new UserPrincipal(userId, username, password, authorities);
        }
    }
}
