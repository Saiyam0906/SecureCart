package com.example.SecureCart.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
	private final CustomUserDetail customUserDetail;
	
	@Bean
	public SecurityFilterChain securityfilter(HttpSecurity http)throws Exception {
		http
		.csrf(c->c.disable())
		.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(a->a
			.requestMatchers("/api/auth/**").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.requestMatchers("/user/**").hasRole("USER")
			 .requestMatchers("/api/email/**").permitAll()
			.anyRequest().authenticated()
			)
		.authenticationProvider(daoAuthenticationProvider())
		.exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint()));
		
		return http.build();
		
		
	}	
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		 DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		    provider.setUserDetailsService(customUserDetail);
		    provider.setPasswordEncoder(passwordEncoder());
		    return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
	    return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder(); 
     }
	
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchyImpl=new RoleHierarchyImpl();
		roleHierarchyImpl.setHierarchy("ROLE_ADMIN > ROLE_USER");
		return roleHierarchyImpl;
	}
	
	@Bean
	public DefaultWebSecurityExpressionHandler expressionHandler(RoleHierarchy roleHierarchy) {
		DefaultWebSecurityExpressionHandler handler=new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchy);
		return handler;
	}
	
	
	 
	 

}
