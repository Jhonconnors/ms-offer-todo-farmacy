package com.example.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IpFilter extends OncePerRequestFilter {

    private final IpSecurityProperties ipProperties;

    public IpFilter(IpSecurityProperties ipProperties) {
        this.ipProperties = ipProperties;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {

        String ip = request.getRemoteAddr();

        if (!ipProperties.getAllowedIps().contains(ip)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("IP not allowed");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
