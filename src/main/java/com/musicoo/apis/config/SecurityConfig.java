package com.musicoo.apis.config;

import com.musicoo.apis.model.MusicooUser;
import com.musicoo.apis.service.Implementation.LoadUserDetailsImpl;
import com.musicoo.apis.service.jwt.AuthTokenFilter;
import com.musicoo.apis.service.jwt.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.data.util.CastUtils.cast;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final AuthTokenFilter authTokenFilter;
    private final LoadUserDetailsImpl loadUserDetails;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
//                .requestMatchers("/api/test").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
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
//        security.cors().and().httpBasic().disable().csrf().disable().authorizeHttpRequests((authz) -> {
//            try {
//                authz
//                        .requestMatchers("/api/test").permitAll()
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .anyRequest().authenticated()
//                        .and()
//                        .sessionManagement()
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        .and()
//                        .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
//                        .and()
//                        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
////                        security
////                                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
////                                .and()
////                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//////                        security.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
//                        security.csrf().disable();
//                        security.headers().frameOptions().disable();
//                        security.httpBasic().disable();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//                .headers().frameOptions().disable();
        return http.build();

    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(cast(loadUserDetails));
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





}
