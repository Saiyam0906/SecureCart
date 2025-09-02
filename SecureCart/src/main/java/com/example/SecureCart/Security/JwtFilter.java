package com.example.SecureCart.Security;


import java.io.IOException;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	
	private final CustomUserDetail customUserDetail;

	@Override
	protected void doFilterInternal(
			@NotNull
			HttpServletRequest request,
			@NotNull
			HttpServletResponse response,
			@NotNull
			FilterChain filterChain) throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		
		if(authHeader==null || !authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		
		try {
			userEmail=jwtService.extractUsername(jwt);
			
			if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication() ==null) {
				UserDetails userDetails=this.customUserDetail.loadUserByUsername(userEmail);
				
				if(jwtService.isTokenValid(userEmail, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
			}
			
			
		}catch (LockedException e) {
			System.err.println("Account locked: " + e.getMessage());
			SecurityContextHolder.clearContext();
		}catch (Exception e) {
			System.err.println("JWT Filter Error: " + e.getMessage());
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}


}
