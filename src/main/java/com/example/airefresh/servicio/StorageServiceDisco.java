package com.example.airefresh.servicio;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Guarda las fotos en una carpeta del disco (propiedad {@code airefresh.fotos}). */
@Service
public class StorageServiceDisco implements StorageService {

    private static final Set<String> TIPOS = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");
    private static final Set<String> EXTENSIONES = Set.of(".jpg", ".jpeg", ".png", ".webp", ".gif");

    private final Path rootLocation;

    public StorageServiceDisco(@Value("${airefresh.fotos:uploads/equipos}") String location) {
        this.rootLocation = Paths.get(location).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo inicializar la carpeta de almacenamiento", e);
        }
    }

    @Override
    public String guardarFoto(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return null;
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !TIPOS.contains(contentType)) {
            throw new ReglaNegocioException("Solo se permiten imágenes JPG, PNG, WEBP o GIF.");
        }

        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            String candidata = nombreOriginal.substring(nombreOriginal.lastIndexOf(".")).toLowerCase(Locale.ROOT);
            if (!EXTENSIONES.contains(candidata)) {
                throw new ReglaNegocioException("Extensión de imagen no permitida.");
            }
            extension = candidata;
        }

        // Nombre único para no sobreescribir imágenes con el mismo nombre
        String nombreGuardado = UUID.randomUUID() + extension;

        try (InputStream entrada = archivo.getInputStream()) {
            Path destino = rootLocation.resolve(nombreGuardado).normalize().toAbsolutePath();
            Files.copy(entrada, destino, StandardCopyOption.REPLACE_EXISTING);
            return nombreGuardado;
        } catch (IOException e) {
            throw new UncheckedIOException("Error al guardar la imagen", e);
        }
    }

    @Override
    public Resource cargarComoRecurso(String nombreFoto) {
        if (nombreFoto == null || nombreFoto.contains("..") || nombreFoto.contains("/") || nombreFoto.contains("\\")) {
            throw new NoEncontradoException("Nombre de archivo inválido");
        }

        Path archivo = rootLocation.resolve(nombreFoto).normalize();
        if (!archivo.startsWith(rootLocation)) {
            throw new NoEncontradoException("Ruta inválida");
        }

        try {
            Resource recurso = new UrlResource(archivo.toUri());
            if (recurso.exists() && recurso.isReadable()) {
                return recurso;
            }
        } catch (MalformedURLException e) {
            throw new NoEncontradoException("Error al buscar el archivo: " + nombreFoto);
        }
        throw new NoEncontradoException("No se puede leer el archivo: " + nombreFoto);
    }
}
