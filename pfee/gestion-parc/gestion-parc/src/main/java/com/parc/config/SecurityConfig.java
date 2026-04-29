package com.parc.config;

import com.parc.security.JwtFilter;
import com.parc.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Lazy
    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/images/**").permitAll()
                .requestMatchers("/api/garages/*/fcm-token").permitAll()
                .requestMatchers("/api/chat/health").permitAll()
                .requestMatchers("/api/conges/**").authenticated()
                .requestMatchers("/api/operateurs-maintenance/demandes").hasRole("OPERATEUR_MAINTENANCE")
                .requestMatchers("/api/operateurs-maintenance/maintenances/*/valider").hasRole("OPERATEUR_MAINTENANCE")
                .requestMatchers("/api/operateurs-maintenance/maintenances/*/planifier").hasRole("OPERATEUR_MAINTENANCE")
                .requestMatchers("/api/operateurs-maintenance/maintenances/*/realiser").hasRole("OPERATEUR_MAINTENANCE")
                .requestMatchers("/api/operateurs-maintenance/maintenances/*/rapporter-probleme").hasRole("OPERATEUR_MAINTENANCE")
                .requestMatchers("/api/operateurs-maintenance/maintenances/*/envoyer-rapport-chef").hasRole("OPERATEUR_MAINTENANCE")
                .requestMatchers("/api/operateurs-maintenance/maintenances/*/rapport-pdf").hasAnyRole("OPERATEUR_MAINTENANCE", "CHEF")
                .requestMatchers("/api/operateurs-maintenance/historique").hasRole("OPERATEUR_MAINTENANCE")
                .requestMatchers("/api/operateurs-maintenance").hasRole("ADMIN")
                .requestMatchers("/api/operateurs-maintenance/*").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(utilisateurService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
