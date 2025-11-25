package com.springboot.backend.andres.usersapp.usersbackend.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.springboot.backend.andres.usersapp.usersbackend.auth.filter.JwtAuthenticationFilter;
import com.springboot.backend.andres.usersapp.usersbackend.auth.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authz -> authz.requestMatchers(HttpMethod.GET, "/api/users", "/api/users/page/{page}")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(ma -> ma.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;

        /*
         * CorsConfiguration configuration = new CorsConfiguration();
         * configuration.addAllowedOrigin("*");
         * configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
         * "OPTIONS"));
         * configuration.setAllowedHeaders(Arrays.asList("Content-Type",
         * "Authorization"));
         * 
         * UrlBasedCorsConfigurationSource source = new
         * UrlBasedCorsConfigurationSource();
         * source.registerCorsConfiguration("/**", configuration);
         * return source;
         */
    }

        /**
     * Registra un CorsFilter en el ApplicationContext.
     *
     * ¿Dónde se usa?
     * - No se llama explícitamente desde este archivo: Spring lo detecta e instancia
     *   automáticamente al arrancar la aplicación porque está anotado con @Bean.
     * - El CorsFilter creado usa la configuración devuelta por corsConfigurationSource().
     * - Al fijar setOrder(Ordered.HIGHEST_PRECEDENCE) nos aseguramos que el filtro
     *   CORS se ejecute antes que los filtros de seguridad (útil para preflight OPTIONS).
     *
     * Nota: puedes confiar solo en http.cors(...) + corsConfigurationSource(), pero
     * mantener este bean garantiza ejecución temprana del filtro.
     */
    @Bean

    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<CorsFilter>(
                new CorsFilter(this.corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
    }
}
