package com.example.airefresh.controlador.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.airefresh.modelo.Equipo;
import com.example.airefresh.servicio.EquipoService;
import com.example.airefresh.servicio.NoEncontradoException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/equipos")
public class EquipoRestController {

    private final EquipoService service;

    public EquipoRestController(EquipoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Equipo> listar() {
        return service.listarActivos();
    }

    @GetMapping("/{id}")
    public Equipo obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .filter(Equipo::isActivo)
                .orElseThrow(() -> new NoEncontradoException("Equipo no encontrado: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Equipo crear(@Valid @RequestBody Equipo equipo) {
        return service.crear(equipo);
    }

    @PutMapping("/{id}")
    public Equipo editar(@PathVariable Long id, @Valid @RequestBody Equipo equipo) {
        return service.editar(id, equipo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrar(@PathVariable Long id) {
        service.borrar(id);
    }
}
