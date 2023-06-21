package com.ChessyBackend.chessy_backend.Token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends GenericFilterBean {

    @Autowired
    public TokenService tokenService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"The token is not valid");
            return;
        }
        final String token = authHeader.substring(7);
        if(tokenService.getUsernameFromToken(token).equals("Sky2")){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"The token is not valid");
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
        return;
    }
}
