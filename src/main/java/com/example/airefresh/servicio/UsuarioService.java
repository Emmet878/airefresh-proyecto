package com.example.airefresh.servicio;

import java.util.Optional;

import com.example.airefresh.modelo.Usuario;

public interface UsuarioService {

    /** Crea una cuenta de cliente. @throws ReglaNegocioException si el correo ya existe */
    Usuario registrar(String nombre, String correo, String clave);

    Optional<Usuario> buscarPorCorreo(String correo);

    long contar();
}
