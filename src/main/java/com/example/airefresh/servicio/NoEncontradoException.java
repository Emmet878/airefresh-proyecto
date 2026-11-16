package com.example.airefresh.servicio;

/** Se pide algo (equipo, pedido, usuario, foto...) que no existe. */
public class NoEncontradoException extends RuntimeException {

    public NoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
