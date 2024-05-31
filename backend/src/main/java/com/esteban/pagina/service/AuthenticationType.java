package com.esteban.pagina.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthenticationType {

    public int type(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if(hasRole(authorities, "Mayorista")){
                return 1;
            } else if(hasRole(authorities, "Encargado")){
                return 2;
            } else if (hasRole(authorities, "Jefe")){
                return 3;
            }
        }
        return 0;
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
