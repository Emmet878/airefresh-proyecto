package com.example.airefresh.controlador.web;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.airefresh.modelo.Usuario;
import com.example.airefresh.servicio.NoEncontradoException;
import com.example.airefresh.servicio.PedidoService;
import com.example.airefresh.servicio.ReglaNegocioException;
import com.example.airefresh.servicio.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PedidoController {

    private static final String TRANSFERENCIA_DEFECTO =
            "Solicita los datos bancarios oficiales al negocio después de confirmar el pedido.";

    private final PedidoService pedidos;
    private final UsuarioService usuarios;

    public PedidoController(PedidoService pedidos, UsuarioService usuarios) {
        this.pedidos = pedidos;
        this.usuarios = usuarios;
    }

    private Usuario usuarioActual(Authentication auth) {
        return usuarios.buscarPorCorreo(auth.getName())
                .orElseThrow(() -> new NoEncontradoException("Usuario no encontrado"));
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Object carrito = session.getAttribute("carrito");

        if (!(carrito instanceof Map<?, ?> mapa) || mapa.isEmpty()) {
            return "redirect:/carrito";
        }

        model.addAttribute(
                "transferencia",
                System.getenv().getOrDefault("AIREFRESH_DATOS_TRANSFERENCIA", TRANSFERENCIA_DEFECTO)
        );
        return "tienda/checkout";
    }

    /** Siempre redirige (Post-Redirect-Get): recargar no duplica el pedido. */
    @PostMapping("/checkout")
    public String confirmar(
            @RequestParam String metodoPago,
            @RequestParam String direccion,
            @RequestParam String telefono,
            @RequestParam(required = false) String notas,
            Authentication auth,
            HttpSession session,
            RedirectAttributes flash
    ) {
        try {
            @SuppressWarnings("unchecked")
            Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");

            var pedido = pedidos.crear(
                    usuarioActual(auth),
                    carrito == null ? Map.of() : carrito,
                    metodoPago, direccion, telefono, notas
            );

            session.removeAttribute("carrito");
            return "redirect:/pedidos/" + pedido.getId() + "?creado";

        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
            flash.addFlashAttribute("direccion", direccion);
            flash.addFlashAttribute("telefono", telefono);
            flash.addFlashAttribute("notas", notas);
            return "redirect:/checkout";
        }
    }

    @GetMapping("/pedidos")
    public String misPedidos(Authentication auth, Model model) {
        model.addAttribute("pedidos", pedidos.paraUsuario(usuarioActual(auth).getId()));
        return "pedidos/lista";
    }

    @GetMapping("/pedidos/{id}")
    public String detalle(@PathVariable Long id, Authentication auth, Model model) {
        var pedido = pedidos.buscarDeUsuario(id, usuarioActual(auth).getId())
                .orElseThrow(() -> new NoEncontradoException("Pedido no encontrado"));

        model.addAttribute("pedido", pedido);
        return "pedidos/detalle";
    }
}
