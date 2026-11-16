
package com.example.airefresh.config;

import com.example.airefresh.repositorio.UsuarioRepository;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(
            UsuarioRepository usuarios
    ) {
        return correo -> {
            var usuario = usuarios.findByCorreoIgnoreCase(correo)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "Usuario no encontrado"
                            )
                    );

            return User.withUsername(usuario.getCorreo())
                    .password(usuario.getClave())
                    .roles(usuario.getRol())
                    .build();
        };
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http.authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                ).permitAll()

                .requestMatchers(
                        "/error",
                        "/acceso-denegado"
                ).permitAll()

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/equipos/**"
                ).permitAll()

                .requestMatchers("/api/**")
                .hasRole("ADMIN")

                .requestMatchers(
                        "/equipos/catalogo",
                        "/",
                        "/login",
                        "/registro",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/equipos/fotos/**",
                        "/productos",
                        "/carrito",
                        "/carrito/**"
                ).permitAll()

                .requestMatchers(
                        "/admin/**",
                        "/equipos/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                        "/pedidos/**",
                        "/checkout/**"
                ).authenticated()

                .anyRequest().authenticated()
        );

        http.formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("correo")
                .passwordParameter("clave")
                .defaultSuccessUrl("/", false)
                .failureUrl("/login?error")
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        http.exceptionHandling(ex -> ex
                .accessDeniedPage("/acceso-denegado")
        );

        return http.build();
    }
}
