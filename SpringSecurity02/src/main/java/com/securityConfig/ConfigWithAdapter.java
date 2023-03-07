package com.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
//Tells Spring that this is Web security configuration
public class ConfigWithAdapter extends WebSecurityConfigurerAdapter {

	// <---------------------- For Authentication ----------------->
	// for authentication we override configure method and we use
	// AuthenticationManagerBuilder class.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("jakas").password("jakas").roles("USER").and().withUser("foo")
				.password("foo").roles("ADMIN");
	}

	// <---------------------- For Authorization ----------------->
	// For Authorization we override configure method and use HttpSecurity class.
	// TO go from more restrictive to less restrictive URL role.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("USER","ADMIN")
			.antMatchers("/").permitAll()
			.and().formLogin();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}

//CLEAR TEXT - means saving password as a string.
