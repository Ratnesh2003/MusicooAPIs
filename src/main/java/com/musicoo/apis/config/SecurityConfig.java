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
//                .oauth2Login().loginPage("/api/oauth/login").userInfoEndpoint().userService(oAuth2UserService)
//                        .and().successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//                        OAuth2UserImpl oAuth2User = (OAuth2UserImpl) authentication.getPrincipal();
//                        String nameParts[] = oAuth2User.getName().split(" ");
//                        RegisterReq registerReq = new RegisterReq(
//                                nameParts[0],
//                                nameParts[1],
//                                oAuth2User.getEmail(),
//                                null,
//                                Provider.GOOGLE
//                        );
//                        System.out.println("Registration Done");
////                            userAuthService.registerUser(registerReq, request);
//                        response.sendRedirect("/api/test");
//                    }
//                });
//        http
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
