package com.esteban.pagina.security;

import com.esteban.pagina.controller.ErrorHandler;
import com.esteban.pagina.service.AuthenticationType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final ErrorHandler errorHandler;
    private final AuthenticationType authType;

    public AuthController(JwtIssuer jwtIssuer, AuthenticationManager authenticationManager, ErrorHandler errorHandler, AuthenticationType authType){
        this.jwtIssuer = jwtIssuer;
        this.authenticationManager = authenticationManager;
        this.errorHandler = errorHandler;
        this.authType = authType;
    }

    @GetMapping("/checkrole")
    public int checkRole() {
        return authType.type();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Validated LoginRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var principal = (UserPrincipal) authentication.getPrincipal();

            var roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            var token = jwtIssuer.issue(principal.getUserId(), principal.getUsername(), roles);

            Map<String, String> jwtResponse = new HashMap<>();
            jwtResponse.put("accessToken", token);

            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.createErrorResponse("Usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
        }
    }
}
