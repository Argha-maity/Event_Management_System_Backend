package com.argha.Event_Management_System.security;

import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String path=request.getServletPath();
        final String jwt;
        final String userEmail;
        final String userId;

        System.out.println("Path: " + path);
        System.out.println("Auth Header: " + (authHeader != null ? authHeader.substring(0, Math.min(authHeader.length(), 50)) + "..." : "null"));

        if(path.equals("/api") || path.equals("/api/auth/register") || path.equals("/api/auth/login")){
            System.out.println("Skipping JWT filter for public endpoint");
            filterChain.doFilter(request, response);
            return;
        }

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            System.out.println("No Bearer token found, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        System.out.println("JWT Token: " + jwt.substring(0, Math.min(jwt.length(), 50)) + "...");

        try{
            userEmail=jwtUtil.extractUsername(jwt);
            userId= String.valueOf(jwtUtil.extractUserId(jwt));
            System.out.println("Extracted email: " + userEmail);
            System.out.println("Extracted id: " + userId);

            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                System.out.println("Loaded user: " + userDetails.getUsername());

                if(jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication successful for user: " + userEmail);
                } else {
                    System.out.println("Token validation failed for user: " + userEmail);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

        }catch(Exception e){
            System.out.println("JWT Authentication error: " + e.getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        System.out.println("Continuing filter chain");
        filterChain.doFilter(request, response); //continue filter chain
    }
}
