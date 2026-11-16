package com.example.airefresh.controlador.api;

import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.airefresh.servicio.NoEncontradoException;
import com.example.airefresh.servicio.ReglaNegocioException;

/** Errores en JSON para los controladores de este paquete (/api/...). */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.example.airefresh.controlador.api")
public class ManejadorErroresApi {

    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(NoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalido(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(Map.of("error", "Datos inválidos"));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, String>> reglaRota(ReglaNegocioException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
