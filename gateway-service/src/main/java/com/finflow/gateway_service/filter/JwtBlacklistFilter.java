package com.finflow.gateway_service.filter;

import com.finflow.gateway_service.security.JwtBlacklistService;
import com.finflow.gateway_service.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final JwtService jwtService ;
    private final JwtBlacklistService blacklistService ;

    public JwtBlacklistFilter(JwtService jwtService, JwtBlacklistService blacklistService) {
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request,response);
            return ;
        }

        String token = authHeader.substring(7);



        if(!jwtService.isValid(token))
        {
            System.out.println("Jwt is invalid");
            filterChain.doFilter(request,response);
            return ;
        }

        boolean blacklisted = blacklistService.isBlacklisted(token);

        if(blacklisted)
        {
            response.setStatus(401);
            response.setContentType("application/json");

            response.getWriter().write(
                    """
                            {
                                "error": "Unauthorized",
                                "message": "Token has been revoked. Please login again."
                            }
                            """
            );
            return ;
        }

        filterChain.doFilter(request,response);

    }
}
