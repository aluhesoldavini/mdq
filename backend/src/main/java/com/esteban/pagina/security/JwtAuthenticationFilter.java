package com.esteban.pagina.security;

import com.esteban.pagina.controller.ErrorHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    private final ErrorHandler errorHandler;


    public JwtAuthenticationFilter(JwtDecoder jwtDecoder, JwtToPrincipalConverter jwtToPrincipalConverter, ErrorHandler errorHandler) {
        this.jwtDecoder = jwtDecoder;
        this.jwtToPrincipalConverter = jwtToPrincipalConverter;
        this.errorHandler = errorHandler;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            extractTokenFromRequest(request)
                    .map(jwtDecoder::decode)
                    .map(jwtToPrincipalConverter::convert)
                    .map(UserPrincipalAuthenticationToken::new)
                    .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        } catch (Exception e) {
            handleExceptionTokenProcess();
        }

        filterChain.doFilter(request, response);
    }

    private ResponseEntity<Object> handleExceptionTokenProcess(){
        return errorHandler.createErrorResponse("Json Web Token inválido", HttpStatus.UNAUTHORIZED);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
