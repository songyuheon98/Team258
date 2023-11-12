package com.example.team258.common.config;

import com.example.team258.common.jwt.JwtAuthenticationFilter;
import com.example.team258.common.jwt.JwtAuthorizationFilter;
import com.example.team258.common.jwt.JwtUtil;
import com.example.team258.common.security.CustomLogoutSuccessHandler;
import com.example.team258.common.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {


    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());  // "/static/**" 으로 들어오는 요청 무시
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers(HttpMethod.POST,"/api/users/**").permitAll() // '/api/users/'로 시작하는 POST 요청 모두 접근 허가
                        .requestMatchers(HttpMethod.GET,"/api/books/search**").permitAll() //
                        .requestMatchers(HttpMethod.GET,"/api/books/{bookId}").permitAll() //

                        .requestMatchers(HttpMethod.GET,"/api/books").permitAll() //

                        // swagger 관련 요청 허용
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()

                        // 조회 API는 비로그인 유저도 접근 가능.
                        .requestMatchers(HttpMethod.GET,"/**").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리

        )
                // 로그아웃 처리 추가 부분
                .logout(logout -> {
                    logout.logoutUrl("/logout") // 로그아웃을 처리할 URL
                            .logoutSuccessUrl("/login") // 로그아웃 성공 시 이동할 페이지 (메인 페이지로 리디렉션)
                            .logoutSuccessHandler(customLogoutSuccessHandler) // CustomLogoutSuccessHandler <- 이부분이 msg : statuscode 반환시킴 설정
                            .invalidateHttpSession(true) // HTTP 세션 무효화 여부
                            .deleteCookies(JwtUtil.AUTHORIZATION_HEADER); // 로그아웃 시 삭제할 쿠키 이름
                });

//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);

//        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}