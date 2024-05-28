package com.lesso.neverland.common.configuration;

import com.lesso.neverland.common.jwt.JwtAuthenticationFilter;
import com.lesso.neverland.common.jwt.JwtExceptionFilter;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;
    private final AuthService authService;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final RedisTemplate<String, String> redisTemplate;

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowedOrigins(List.of("http://localhost:8080")); //TODO: 프론트 로컬, 배포 url 추가
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/users/login"),
                                new AntPathRequestMatcher("/users/signup"),
                                new AntPathRequestMatcher("/users/nickname"),
                                new AntPathRequestMatcher("/users/loginId"),
                                new AntPathRequestMatcher("/users/reissue-token"),
                                new AntPathRequestMatcher("/**", "GET"),
                                new AntPathRequestMatcher("/gpt3/completePuzzle"),
                                new AntPathRequestMatcher("/home", "GET")).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(authService, userService, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .build();
    }
}