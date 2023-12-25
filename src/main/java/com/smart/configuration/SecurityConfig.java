package com.smart.configuration;

import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
				.loginProcessingUrl("/dologin")
				.successHandler(new CustomAuthenticationSuccessHandler())
//				.failureUrl("/login")
				).authorizeHttpRequests(auth->auth
			.requestMatchers("/admin/**")
			.hasRole("ADMIN")
			.requestMatchers("/user/**")
			.hasRole("USER")
			.requestMatchers("/**")
			.permitAll()
			.anyRequest().authenticated())
			.logout((logout) -> 
					logout.deleteCookies("remove")
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.clearAuthentication(true)
 					.invalidateHttpSession(true)
 					.logoutSuccessUrl("/login?logout")
					.permitAll()).build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
}
