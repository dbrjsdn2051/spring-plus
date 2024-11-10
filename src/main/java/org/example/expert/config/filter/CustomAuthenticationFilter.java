package org.example.expert.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.config.JwtUtil;
import org.example.expert.config.security.AuthUser;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        setFilterProcessesUrl("/auth/signin");
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SigninRequest loginUser = mapper.readValue(request.getInputStream(), SigninRequest.class);

            UsernamePasswordAuthenticationToken unauthenticated
                    = UsernamePasswordAuthenticationToken.unauthenticated(loginUser.getEmail(), loginUser.getPassword());
            return this.getAuthenticationManager().authenticate(unauthenticated);
        } catch (IOException e) {
            throw new AuthenticationServiceException("인증정보가 잘못되었습니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        SecurityContextHolder.getContext().setAuthentication(authResult);
        AuthUser authUser = (AuthUser) authResult.getPrincipal();
        String token = jwtUtil.createToken(authUser.getId(), authUser.getEmail(), authUser.getRole(), authUser.getNickname());

        response.addHeader("Authorization", token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        response.getWriter().write(mapper.writeValueAsString(new SigninResponse(token)));

        log.info("JWT Token 발급 성공: {}", token);
    }
}
