package com.config;

import org.apache.catalina.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity //Used so, that we can user method level authentication.
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//  BCrypt, however, will internally generate a random salt instead.
//	This is important to understand because it means that each call will have a different result, 
//	so we only need to encode the password once.

	@Bean
	public UserDetailsService userDetailsService() {
	
		UserDetails normalUser = org.springframework.security.core.userdetails.User.withUsername("normal")
				.password(passwordEncoder().encode("password")).roles("NORMAL").build();

		UserDetails adminUser = org.springframework.security.core.userdetails.User.withUsername("admin")
				.password(passwordEncoder().encode("password")).roles("ADMIN").build();

		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(normalUser, adminUser);
		return inMemoryUserDetailsManager;
//		return new CustomUserDetailsService();
	}
	
// ** means anything further in URL will can be user by that role.
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
		.authorizeHttpRequests()
//		.requestMatchers("/home/admin")
//		.hasRole("ADMIN")
//		.requestMatchers("/home/normal")
//		.hasRole("NORMAL")
//		.requestMatchers("/home/public")
//		.permitAll()
		.anyRequest()
				.authenticated()
				.and()
				.formLogin();

		return httpSecurity.build();
	}
}
// CSRF (Cross-Site Request Forgery)
// CSRF - If you are only creating a service that is used by non-browser clients,
//			you will likely want to disable CSRF protection


//In Memory UserDetails Manager
// Non-persistent implementation of UserDetailsManager which is backed by an in-memory map. Mainly intended for testing and demonstration purposes, 
//where a full blown persistent system isn't required.