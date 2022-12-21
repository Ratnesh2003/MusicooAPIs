package com.musicoo.apis.config;



import com.musicoo.apis.service.jwt.AuthTokenFilter;
import com.musicoo.apis.service.jwt.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final AuthTokenFilter authTokenFilter;
//    private final UserAuthServiceImpl userAuthService;
//    private final OAuth2UserServiceImpl oAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
//                .anyRequest().permitAll()
                .requestMatchers("/api/test").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
                http.csrf().disable();
                http.headers().frameOptions().disable();
        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





}
