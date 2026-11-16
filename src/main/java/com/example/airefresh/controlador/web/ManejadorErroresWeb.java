package com.example.airefresh.controlador.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.airefresh.servicio.NoEncontradoException;
import com.example.airefresh.servicio.ReglaNegocioException;

/**
 * Convierte los errores de las paginas web en una pantalla amable
 * (en lugar de la pagina de error por defecto).
 */
@ControllerAdvice(annotations = Controller.class)
public class ManejadorErroresWeb {

    private static final Logger log = LoggerFactory.getLogger(ManejadorErroresWeb.class);

    @ExceptionHandler(NoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noEncontrado(NoEncontradoException e, Model model) {
        return vista(model, 404, "No encontramos lo que buscas",
                "El elemento solicitado no existe o ya no está disponible.");
    }

    @ExceptionHandler(ReglaNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String reglaRota(ReglaNegocioException e, Model model) {
        return vista(model, 400, "No pudimos completar la acción", e.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String parametrosInvalidos(Exception e, Model model) {
        return vista(model, 400, "Solicitud no válida", "Revisa los datos enviados e inténtalo de nuevo.");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public String archivoGrande(MaxUploadSizeExceededException e, Model model) {
        return vista(model, 413, "Archivo demasiado grande", "La imagen supera el máximo permitido de 10 MB.");
    }

    /** Recargar o abrir por GET una URL que solo acepta POST: se vuelve al inicio sin error. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String metodoNoPermitido(HttpRequestMethodNotSupportedException e) {
        log.debug("Metodo no permitido: {}", e.getMessage());
        return "redirect:/";
    }

    private String vista(Model model, int status, String titulo, String mensaje) {
        model.addAttribute("status", status);
        model.addAttribute("titulo", titulo);
        model.addAttribute("mensaje", mensaje);
        return "error";
    }
}
