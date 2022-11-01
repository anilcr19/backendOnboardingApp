package com.thecodeveal.app.config;

import javax.swing.CellEditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.servlet.HttpBasicDsl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.thecodeveal.app.service.CustomUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	@Autowired
	CustomUserService userService;
	
	@Autowired
	JWTTokenHelper jwtTokenHelper;
	
	@Autowired
	RestAuthentication restAuthentication;
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
	
		auth.inMemoryAuthentication().withUser("charan").password(passwordEncoder().encode("charan@464")).authorities("USER","ADMIN");
		
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
		
		
		
		
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		
		//http.authorizeRequests((request)->request.antMatchers("/h2-console/**").permitAll().anyRequest().authenticated()).httpBasic();
		
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
		and().exceptionHandling().authenticationEntryPoint(restAuthentication).and().authorizeRequests(request->
		request.antMatchers("/h2-console/**","/api/v1/**","/generatemail/**","/offerstatus/**","/saveDocuments/**","/addCandidate/**","/details/**","/userdetails/**","/bankDetails/**","/changepassowrd").permitAll()).addFilterBefore(new JWTAuthenticationFilter(userService, jwtTokenHelper),
				UsernamePasswordAuthenticationFilter.class);
		
		
		
		http.formLogin();
		
		http.csrf().disable().headers().frameOptions().disable();
		
		http.httpBasic();
//		
	}


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	
	

}
