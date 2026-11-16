package com.example.airefresh.config;

import com.example.airefresh.modelo.Usuario;
import com.example.airefresh.repositorio.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {

    @Bean
    CommandLineRunner crearAdminInicial(
            UsuarioRepository usuarios,
            PasswordEncoder encoder
    ) {
        return args -> {
            String correo = System.getenv("AIREFRESH_ADMIN_EMAIL");
            String clave = System.getenv("AIREFRESH_ADMIN_PASSWORD");

            boolean credencialesValidas =
                    correo != null
                    && !correo.isBlank()
                    && clave != null
                    && clave.length() >= 12;

            if (credencialesValidas
                    && !usuarios.existsByCorreoIgnoreCase(correo)) {

                Usuario usuario = new Usuario();

                usuario.setNombre("Administrador Airefresh");
                usuario.setCorreo(correo.trim().toLowerCase());
                usuario.setClave(encoder.encode(clave));
                usuario.setRol("ADMIN");

                usuarios.save(usuario);
            }
        };
    }
}
