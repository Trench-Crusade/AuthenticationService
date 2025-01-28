package org.tc.authservice.shared.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:9000"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));

                    configuration.setAllowedHeaders(List.of("*"));

                    configuration.setExposedHeaders(List.of("x-auth-token"));
                    configuration.setAllowCredentials(true);

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);

                    cors.configurationSource(source);
                })
//                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth -> oauth.defaultSuccessUrl("/authentication/loginByGoogle", true))
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request.requestMatchers("/**").permitAll());
        log.info(http.oauth2Login(Customizer.withDefaults()).toString());
        return http.getOrBuild();
    }

}
