package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		// DAO class dung de retrieve data from database -  dung nhu 1 adapter authentication provider
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// set authentication provider dung de authentication
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/users/**").hasAuthority("Admin")
				.antMatchers("/setting/**").hasAuthority("Admin")
				.antMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
				.antMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
				.antMatchers("/products/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
				.antMatchers("/customers/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
				.antMatchers("/customers/**").hasAnyAuthority("Admin","Salesperson")
				.antMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")
				.antMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson")
				.antMatchers("/reports/**").hasAnyAuthority("Admin","Salesperson")
				.antMatchers("/articles/**").hasAnyAuthority("Admin","Editor")
				.antMatchers("/menus/**").hasAnyAuthority("Admin","Editor")
				.anyRequest().authenticated()
				.and().formLogin().loginPage("/login").usernameParameter("email").permitAll()
				.and().logout().permitAll()
				.and().rememberMe().key("AbcDEfghiklmnos_123456789").tokenValiditySeconds(7 * 24 * 60 * 60);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}
	
}
