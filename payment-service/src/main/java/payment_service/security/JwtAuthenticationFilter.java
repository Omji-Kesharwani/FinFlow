package payment_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService ;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return ;
        }

        String token = authHeader.substring(7);

        if(!jwtService.isValid(token))
        {
            filterChain.doFilter(request,response);
            return ;
        }



        UUID userId = jwtService.extractUserId(token);
//        System.out.println("JWT authenticated user: " + userId);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

//        System.out.println("Request allowed by JWT filter: "
//                + request.getMethod()
//                + " "
//                + request.getRequestURI());
        filterChain.doFilter(request,response);
    }
}
