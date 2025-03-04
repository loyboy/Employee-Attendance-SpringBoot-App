package com.example.empattendance.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
       
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getOutputStream().print("{\"error\":\"Unauthorized.. Please authenticate..\"}");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
