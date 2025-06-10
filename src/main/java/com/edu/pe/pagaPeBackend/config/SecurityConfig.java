package com.edu.pe.pagaPeBackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and()
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/api/pagaPe/v1/auth/**","/api/pagaPe/v1/auth/login","/api/pagaPe/v1/users/nameAndEmail","/api/excel/**","/api/ai-training/**","/api/sharepoint/**","/api/cosapi/v1/users/**")
                                .permitAll()
                        // Swagger UI y OpenAPI
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                                .permitAll()
                        // API de clientes (permitir acceso para pruebas)
                        .requestMatchers("/api/clients/**")
                                .permitAll()
                        // Otros endpoints protegidos
                        .requestMatchers(HttpMethod.GET, "/api/pagaPe/v1/users/me","/api/pagaPe/v1/users/nameAndEmail")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/pagaPe/v1/auth/login").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/pagaPe/v1/users/{userId}").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/pagaPe/v1/users/{userId}").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/api/pagaPe/v1/users/{userId}").hasAnyAuthority("ADMIN")
                        .requestMatchers(
                                "/api/thebigfun/v1/users/{userId}/addImage",
                                "/api/betabyte/v1/rents",
                                "/api/betabyte/v1/cards",
                                "/api/betabyte/v1/bicycles/**").authenticated()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        // cuando se establece en STATELESS, significa que no se creará ni
                        // mantendrá ninguna sesión HTTP en el servidor.
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}