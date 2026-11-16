package com.example.airefresh.controlador.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.airefresh.controlador.dto.CarritoDTO;
import com.example.airefresh.servicio.CarritoService;
import com.example.airefresh.servicio.ReglaNegocioException;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritos;

    public CarritoController(CarritoService carritos) {
        this.carritos = carritos;
    }

    /** El carrito vive en la sesión: id de equipo -> cantidad. */
    @SuppressWarnings("unchecked")
    private Map<Long, Integer> carrito(HttpSession session) {
        Object actual = session.getAttribute("carrito");
        if (actual instanceof Map<?, ?>) {
            return (Map<Long, Integer>) actual;
        }
        Map<Long, Integer> nuevo = new LinkedHashMap<>();
        session.setAttribute("carrito", nuevo);
        return nuevo;
    }

    @GetMapping
    public String ver(HttpSession session, Model model) {
        CarritoDTO resumen = carritos.resumen(carrito(session));
        model.addAttribute("lineas", resumen.getLineas());
        model.addAttribute("total", resumen.getTotal());
        return "tienda/carrito";
    }

    @PostMapping("/agregar/{id}")
    public String agregar(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer cantidad,
            HttpSession session,
            RedirectAttributes flash
    ) {
        try {
            carritos.agregar(carrito(session), id, cantidad);
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            HttpSession session,
            RedirectAttributes flash
    ) {
        try {
            carritos.actualizar(carrito(session), id, cantidad);
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/quitar/{id}")
    public String quitar(@PathVariable Long id, HttpSession session) {
        carritos.quitar(carrito(session), id);
        return "redirect:/carrito";
    }
}
