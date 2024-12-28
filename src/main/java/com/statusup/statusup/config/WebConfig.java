package com.statusup.statusup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Check if an environment variable exists for the frontend URL
                String frontendUrl = System.getenv("FRONTEND_URL");

                // If the environment variable is not set, default to localhost (for local development)
                if (frontendUrl == null || frontendUrl.isEmpty()) {
                    frontendUrl = "http://81.221.83.72:3000";
                }

                // Allow requests from the frontend (whether it's localhost or Heroku)
                registry.addMapping("/**")
                        .allowedOrigins(frontendUrl)  // Allow React app's origin (local or Heroku)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
