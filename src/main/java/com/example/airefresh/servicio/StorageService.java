package com.example.airefresh.servicio;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/** Guarda y recupera las fotos de los equipos. */
public interface StorageService {

    /**
     * Guarda la imagen con un nombre único y devuelve ese nombre.
     *
     * @return el nombre guardado, o {@code null} si el archivo venía vacío
     * @throws ReglaNegocioException si no es una imagen JPG, PNG, WEBP o GIF
     */
    String guardarFoto(MultipartFile archivo);

    /** @throws NoEncontradoException si el nombre no es válido o el archivo no existe */
    Resource cargarComoRecurso(String nombreFoto);
}
