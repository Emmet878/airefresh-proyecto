package com.example.airefresh.controlador.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.airefresh.servicio.EquipoService;

/** Portada y catálogo de la tienda. */
@Controller
public class InicioController {

    private final EquipoService equipos;

    public InicioController(EquipoService equipos) {
        this.equipos = equipos;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("equipos", equipos.destacados());
        return "tienda/inicio";
    }

    @GetMapping("/productos")
    public String productos(
            @RequestParam(required = false) List<Integer> btu,
            @RequestParam(required = false) List<String> tipo,
            @RequestParam(required = false) List<String> marca,
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max,
            Model model
    ) {
        model.addAttribute("equipos", equipos.filtrar(btu, tipo, marca, min, max));
        return "tienda/catalogo";
    }
}
