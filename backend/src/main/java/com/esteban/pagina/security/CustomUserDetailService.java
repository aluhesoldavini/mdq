package com.esteban.pagina.security;

import com.esteban.pagina.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.getUserByName(username).orElseThrow();
        String userRole;
        int userType = user.getTipo();

        if(userType == 1){
            userRole = "Mayorista";
        } else if(userType == 2){
            userRole = "Encargado";
        } else {
            userRole = "Jefe";
        }

        return UserPrincipal.builder()
                .userId(user.getIdUsuario())
                .username(user.getNombre())
                .authorities(List.of(new SimpleGrantedAuthority(userRole)))
                .password(user.getContrasena())
                .build();
    }
}
