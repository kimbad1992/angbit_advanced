package com.angbit.angbit_advanced.common.config;


import com.angbit.angbit_advanced.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /* 경로별 권한 설정 */
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                    .requestMatchers(
                            new AntPathRequestMatcher("/bootstrap/**/**"),
                            new AntPathRequestMatcher("/favicon/**")
                    )
                    .permitAll()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()) // 정적 자원에 대해 허용, (/css/**, /js/**, /images/**, /webjars/**, /**/favicon.ico)
                    .permitAll()
                    .requestMatchers(
                            // Swagger
                            new AntPathRequestMatcher("/v3/api-docs/**"),
                            new AntPathRequestMatcher("/swagger-ui/**"),
                            new AntPathRequestMatcher("/swagger-resources/**"),
                            // H2 Console
                            new AntPathRequestMatcher("/h2-console/**")
                    )
                    .permitAll()
                    .anyRequest().authenticated()
            )
            .headers(headers -> headers
                    // 동일 출처에서의 프레임 사용을 허용 (H2Console)
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .csrf(csrf -> csrf
                /*
                CSRF 토큰 프로바이더를 설정하지 않으면
                스프링 시큐리티의 기본 설정은 HttpSessionCsrfTokenRepository를 사용함
                혹은 아래와 같은 쿠키 기반 CSRF 토큰 프로바이더를 설정할 수도 있음
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                */
                .ignoringRequestMatchers(
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/login_request"),
                        new AntPathRequestMatcher("/logout"),
                        new AntPathRequestMatcher("/h2-console/**")
                )
            )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // 로그인 페이지 경로 설정
                        .loginProcessingUrl("/login_request") // 로그인 처리 경로 설정
                        .defaultSuccessUrl("/main", true) // 로그인 성공 후 기본 리디렉션 경로 설정
                        .failureUrl("/login?error=true") // 로그인 실패 후 리디렉션 경로 설정
                        .permitAll()
                )
            .logout(logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")
            )
            .rememberMe(rememberMe -> rememberMe
                .key("payroll")
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .userDetailsService(memberService)
                .rememberMeParameter("remember-me")
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionFixation()
                    .changeSessionId()
                .maximumSessions(1)
//                    .expiredSessionStrategy()
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/login?expired")
            );

        return http.build();
    }

    // CORS 설정 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Content-Type", "Authorization", "X-CSRF-TOKEN"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

}
