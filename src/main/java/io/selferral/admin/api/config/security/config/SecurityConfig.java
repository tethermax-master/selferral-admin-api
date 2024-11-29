package io.selferral.admin.api.config.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.selferral.admin.api.config.security.JwtAuthenticationFilter;
import io.selferral.admin.api.config.security.exception.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	@Description("패스워드 암호화")
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
	    security.httpBasic(HttpBasicConfigurer::disable)
	    			.csrf(CsrfConfigurer::disable)
	    			.cors(Customizer.withDefaults())
//	    			cors(cors -> cors.disable())
//	    			.formLogin(Customizer.withDefaults())
	    			.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	    			.authorizeHttpRequests(request -> 
//	    				request.requestMatchers("/ttmax/").hasRole("USER")
//	    					   .requestMatchers("/admin").hasRole("ADMIN")
		    			request.requestMatchers("/sign/**").permitAll()
		    				   .anyRequest().authenticated())
	    			.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
//	    			.addFilterBefore(jwtExceptionHandlerFilter, JwtAuthenticationFilter.class);
	    			.exceptionHandling(authenticationManager -> authenticationManager 
	    					.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
//	    					.accessDeniedHandler(new CustomAccessDeniedHandler()) // accessDeniedException 은 servlet 뒤에서 발생하기때문에 GlobalExceptionHandler 에서 처리한다.
	    return security.build();
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); 

        /* TODO 도메인 나오면 수정 */
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://admin.selferral.io", "https://admin-api.selferral.io", "https://selferral-admin-web.vercel.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); 
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}