package com.example.airefresh.controlador.web;

import java.time.Duration;

import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.airefresh.modelo.Equipo;
import com.example.airefresh.servicio.EquipoService;
import com.example.airefresh.servicio.NoEncontradoException;
import com.example.airefresh.servicio.ReglaNegocioException;
import com.example.airefresh.servicio.StorageService;

import jakarta.validation.Valid;

/** Inventario de equipos (solo administrador) y servicio de las fotos. */
@Controller
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService service;
    private final StorageService storage;

    public EquipoController(EquipoService service, StorageService storage) {
        this.service = service;
        this.storage = storage;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String marca, Model model) {
        model.addAttribute(
                "equipos",
                marca == null || marca.isBlank()
                        ? service.listar()
                        : service.buscarPorMarca(marca)
        );
        return "equipos/listar";
    }

    @GetMapping("/catalogo")
    public String catalogo() {
        return "redirect:/productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Equipo equipo = new Equipo();
        equipo.setExistencias(0);
        model.addAttribute("equipo", equipo);
        return "equipos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute Equipo equipo,
            BindingResult result,
            @RequestParam("archivoFoto") MultipartFile archivo,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Revisa los campos ingresados.");
            return "equipos/formulario";
        }

        if (equipo.getFoto() != null && equipo.getFoto().isBlank()) {
            equipo.setFoto(null);
        }

        if (!archivo.isEmpty()) {
            try {
                equipo.setFoto(storage.guardarFoto(archivo));
            } catch (ReglaNegocioException e) {
                model.addAttribute("error", e.getMessage());
                return "equipos/formulario";
            }
        }

        if (equipo.getId() == null) {
            service.crear(equipo);
        } else {
            service.editar(equipo.getId(), equipo);
        }
        return "redirect:/equipos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Equipo equipo = service.buscarPorId(id)
                .orElseThrow(() -> new NoEncontradoException("Equipo no encontrado"));
        model.addAttribute("equipo", equipo);
        return "equipos/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.borrar(id);
        return "redirect:/equipos";
    }

    /** Si la foto no existe en disco responde 404 en vez de un error 500. */
    @GetMapping("/fotos/{nombreFoto:.+}")
    public ResponseEntity<Resource> foto(@PathVariable String nombreFoto) {
        try {
            Resource recurso = storage.cargarComoRecurso(nombreFoto);
            MediaType tipo = MediaTypeFactory.getMediaType(recurso)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .contentType(tipo)
                    .cacheControl(CacheControl.maxAge(Duration.ofDays(7)).cachePublic())
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + recurso.getFilename() + "\"")
                    .body(recurso);
        } catch (NoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
