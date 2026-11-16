package com.example.airefresh.controlador.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.airefresh.servicio.EquipoService;
import com.example.airefresh.servicio.PedidoService;
import com.example.airefresh.servicio.ReglaNegocioException;
import com.example.airefresh.servicio.UsuarioService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EquipoService equipos;
    private final UsuarioService usuarios;
    private final PedidoService pedidos;

    public AdminController(EquipoService equipos, UsuarioService usuarios, PedidoService pedidos) {
        this.equipos = equipos;
        this.usuarios = usuarios;
        this.pedidos = pedidos;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalEquipos", equipos.contar());
        model.addAttribute("totalUsuarios", usuarios.contar());
        model.addAttribute("pedidos", pedidos.todos());
        model.addAttribute("estados", pedidos.estados());
        return "admin/dashboard";
    }

    @PostMapping("/pedidos/{id}/estado")
    public String estado(@PathVariable Long id, @RequestParam String estado, RedirectAttributes flash) {
        try {
            pedidos.actualizarEstado(id, estado);
            flash.addFlashAttribute("mensaje", "Pedido #" + id + " actualizado a " + estado + ".");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
