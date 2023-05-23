package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    // userDetailsService is used by authenticationManager regardless of what IntelliJ says
//    private final UserDetailsService userDetailsService;
//    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
//    private final JwtAuthenticationFilter authenticationFilter;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    private final CognitoSubClaimAdapter cognitoSubClaimAdapter;

    public SecurityConfig(CognitoSubClaimAdapter cognitoSubClaimAdapter) {
        this.cognitoSubClaimAdapter = cognitoSubClaimAdapter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .antMatchers("/api/v1/test/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt().decoder(jwtDecoder()));
                //.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        decoder.setClaimSetConverter(cognitoSubClaimAdapter);

        return decoder;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTION"));
        corsConfig.addAllowedHeader("*");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
