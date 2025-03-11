package org.rentacar1.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()) // Разрешава всички заявки
                .formLogin(form -> form.disable())      // Изключва login форма
                .httpBasic(httpBasic -> httpBasic.disable()) // Изключва Basic Auth
                .csrf(csrf -> csrf.disable());          // Изключва CSRF (само за тестове)
        return http.build();
    }
}

