package com.example.ojt.security.jwt;


import com.example.ojt.security.principle.AccountDetailsServiceCustom;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTAuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private AccountDetailsServiceCustom detailsServiceCustom;
    @Autowired
    private JWTProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Point 1
        // xu lí token và giải mã -> cấp quyền / chứng nhân SecurityContext
        // lấy tokken
        String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)){
            String username = jwtProvider.getUserNameFromToken(token);
            UserDetails userDetails = detailsServiceCustom.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            // lưu authentication vào security context
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
         filterChain.doFilter(request,response);
    }
    private String getTokenFromRequest(HttpServletRequest request){
        // lấy ra chuỗi bearer token
        String bearer = request.getHeader("Authorization");
        if (bearer!=null && bearer.startsWith("Bearer ")){
            return bearer.substring("Bearer ".length());
        }
        return null;
    }
}
