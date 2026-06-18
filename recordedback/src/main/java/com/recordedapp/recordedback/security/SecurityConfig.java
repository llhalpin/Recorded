package com.recordedapp.recordedback.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.Arrays;


@Configuration
@EnableMethodSecurity //llh 7-23-2025 need this for annotation PreAuthorize to verify username
//3-12-2026 using this class instead of CorsConfig after moving to Vite from CRA
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    //3-12-2026 changed the localhost to 5173 from 3000 since that is what Vite uses
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("🌐 Configuring CORS...");
        CorsConfiguration configuration = new CorsConfiguration();

        // Use specific origins - NO wildcards when credentials are enabled
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        System.out.println("✅ CORS configured for origin: http://localhost:5173");
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Configure CORS
        http.cors(cors -> {
            System.out.println("🌐 Enabling CORS with custom configuration...");
            cors.configurationSource(corsConfigurationSource());
        });

        // Disable CSRF (we're using JWTs, not cookies)
        http.csrf(csrfConfig -> {
            System.out.println("🔒 Disabling CSRF protection...");
            csrfConfig.disable();
        });

        // Set session creation policy to STATELESS
        http.sessionManagement(sessionConfig -> {
            System.out.println("📦 Setting session management to STATELESS...");
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        // Set up authorization rules
        http.authorizeHttpRequests(authConfig -> {
            System.out.println("🌐 Allowing OPTIONS requests for CORS preflight...");
            authConfig.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

            System.out.println("✅ Allowing public access to /api/auth/**..."); //llh 06-02-2025 corrected
            authConfig.requestMatchers("/api/auth/**").permitAll();  //llh 06-02-2025 corrected from api/auth/**

            System.out.println("🔐 Requiring authentication for all other requests...");
            authConfig.anyRequest().authenticated();
        });

        // Add custom JWT filter before the default Spring login filter
        System.out.println("🧱 Adding JWT filter before UsernamePasswordAuthenticationFilter...");
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the configured filter chain
        System.out.println("🚀 Security filter chain ready to go!");
        return http.build();
    }
}