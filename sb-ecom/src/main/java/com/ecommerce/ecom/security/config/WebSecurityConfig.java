package com.ecommerce.ecom.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.ecom.security.jwt.AuthEntryPointJwt;
import com.ecommerce.ecom.security.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf->csrf.disable());
		http.exceptionHandling(exception-> exception.authenticationEntryPoint(unauthorizedHandler));
		http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(auth->auth.requestMatchers("/api/auth/**").permitAll()
											  .requestMatchers("/images").permitAll()
											  .anyRequest().authenticated()
		);
		http.authenticationProvider(authenticationProvider());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web-> web.ignoring().requestMatchers(
				"/v2/api-docs",
				"/configuration/ui",
				"/swagger-resouceses/**",
				"/configuration/security",
				"swagger-ui.html",
				"/webjars/**"
		));
	}
}
