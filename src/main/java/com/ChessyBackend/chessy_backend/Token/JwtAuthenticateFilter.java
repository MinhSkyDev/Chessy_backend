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

    @Autowired
    public TokenRepository tokenRepository;
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
        try{
            //Exception may occur in this line, when the Token is expired
            String username = tokenService.getUsernameFromToken(token);
            TokenModel tokenModel = tokenRepository.getToken(username);

            if(tokenModel.accessToken.equals(token) || tokenModel.refreshToken.equals(token)){
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else{
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"The token is not valid");
                return;
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }
        catch(Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"The token is not valid");
            return;
        }
        return;
    }
}
