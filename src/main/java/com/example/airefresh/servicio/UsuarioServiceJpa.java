package com.example.airefresh.servicio;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airefresh.modelo.Usuario;
import com.example.airefresh.repositorio.UsuarioRepository;

@Service
public class UsuarioServiceJpa implements UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioServiceJpa(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public Usuario registrar(String nombre, String correo, String clave) {
        String correoNormal = correo.trim().toLowerCase();

        if (repo.existsByCorreoIgnoreCase(correoNormal)) {
            throw new ReglaNegocioException("Ya existe una cuenta con ese correo.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.trim());
        usuario.setCorreo(correoNormal);
        usuario.setClave(encoder.encode(clave));
        usuario.setRol("CLIENTE");

        return repo.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return repo.findByCorreoIgnoreCase(correo);
    }

    @Override
    @Transactional(readOnly = true)
    public long contar() {
        return repo.count();
    }
}
