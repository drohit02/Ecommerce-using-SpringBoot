package com.ecommerce.ecom.security.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.ecom.security.jwt.AuthEntryPointJwt;
import com.ecommerce.ecom.security.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	 @Bean
	    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	        http.authorizeHttpRequests(authorizeRequests ->authorizeRequests.requestMatchers("/signin").permitAll()
	                        .anyRequest().authenticated());
	        http.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
	        //http.httpBasic(withDefaults());
	        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
	        http.csrf(csrf -> csrf.disable());
	        http.addFilterBefore(authenticationJwtTokenFilter(),UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}

//	@Bean
//	UserDetailsService userDetailsService() {
//		UserDetails user1 = User.withUsername("user1").password(passwordEncoder().encode("pass123")).roles("USER")
//				.build();
//
//		UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN")
//				.build();
//
//		return new InMemoryUserDetailsManager(user1, admin);
//	}

    @Bean
    UserDetailsService userDetailsService() {
    	UserDetails user1 = User.withUsername("user1")
    							.password(passwordEncoder().encode("pass123"))
    							.roles("USER")
    							.build();
    	
    	UserDetails admin1 = User.withUsername("admin1")
				.password(passwordEncoder().encode("admin123"))
				.roles("ADMIN")
				.build();

    	JdbcUserDetailsManager jdbcUserDetailsManager  = new JdbcUserDetailsManager(dataSource);
    	jdbcUserDetailsManager.createUser(user1);
    	jdbcUserDetailsManager.createUser(admin1);
    	return jdbcUserDetailsManager;
    }
    
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

}
