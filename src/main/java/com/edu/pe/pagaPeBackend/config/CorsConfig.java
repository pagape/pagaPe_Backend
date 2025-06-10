package com.edu.pe.pagaPeBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/cosapi/v1/**")
                        .allowedOrigins(
                            "http://localhost:4200",
                            "https://pagape-frontend.azurewebsites.net",
                            "https://ambitious-water-0d42bc910.6.azurestaticapps.net/"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        //.allowedOrigins("*")
                        //.allowedMethods("*")
                        .allowedHeaders("*");



            }
        };
    }
}
