package com.zerobase.used_trade.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
  //권한 필요 없는 페이지들
  private static final String[] AUTH_WHITELIST = {
      "/swagger-ui/**", "/swagger-ui-custom.html",
      "/domain/**", "/user/sign-up", "/user/sign-in", "/user/detail/public/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(request -> request
            //TODO token 사용 이후 @PreAuthorization 사용 예정이라 모든 경로에 대한 인증처리는 PASS
            //TODO .requestMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().permitAll() //authenticated()
            );

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(
        "/h2-console/**",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/v3/api-docs/**");
  }
}
