package com.example.airefresh.servicio;

/** Se intenta algo que las reglas de la tienda no permiten (sin stock, correo repetido, estado inválido...). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
