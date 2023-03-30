package com.backend.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.defaultContentType(MediaType.TEXT_PLAIN);
//    }

//    String url = "https://shopping-center-lime.vercel.app";
//    private final String[] allowedOrigins = new String[]{"http://localhost:3000"};
//    private final String[] allowedOrigins = new String[]{"https://shopping-center-lime.vercel.app"};
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins(allowedOrigins)
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*")
////                .exposedHeaders("Authorization")
//                .allowCredentials(true);
////                .allowedHeaders("Authorization");
//    }
}
