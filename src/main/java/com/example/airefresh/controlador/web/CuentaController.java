package com.example.airefresh.controlador.web;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.airefresh.servicio.ReglaNegocioException;
import com.example.airefresh.servicio.UsuarioService;

/** Inicio de sesión, registro de cuenta y pantalla de acceso denegado. */
@Controller
public class CuentaController {

    private static final Pattern CORREO =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final UsuarioService usuarios;

    public CuentaController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
        }
        return "cuenta/acceso";
    }

    @GetMapping("/registro")
    public String registro() {
        return "cuenta/acceso";
    }

    /**
     * Patron Post-Redirect-Get: tras enviar el formulario siempre se redirige,
     * asi recargar la pagina no vuelve a enviar el registro.
     */
    @PostMapping("/registro")
    public String crearCuenta(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String clave,
            @RequestParam String clave2,
            RedirectAttributes flash
    ) {
        String error = validarRegistro(nombre, correo, clave, clave2);

        if (error == null) {
            try {
                usuarios.registrar(nombre, correo, clave);
            } catch (ReglaNegocioException | DataIntegrityViolationException e) {
                error = e instanceof ReglaNegocioException
                        ? e.getMessage()
                        : "Ya existe una cuenta con ese correo.";
            }
        }

        if (error != null) {
            flash.addFlashAttribute("errorRegistro", error);
            flash.addFlashAttribute("nombreForm", nombre);
            flash.addFlashAttribute("correoForm", correo);
            return "redirect:/registro";
        }

        flash.addFlashAttribute("registroExitoso", "Cuenta creada. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }

    @GetMapping("/acceso-denegado")
    public String denegado() {
        return "cuenta/acceso-denegado";
    }

    private String validarRegistro(String nombre, String correo, String clave, String clave2) {
        if (nombre.isBlank() || correo.isBlank()) {
            return "Completa todos los datos.";
        }
        if (nombre.trim().length() > 100) {
            return "El nombre es demasiado largo (máximo 100 caracteres).";
        }
        if (correo.trim().length() > 180 || !CORREO.matcher(correo.trim()).matches()) {
            return "Escribe un correo electrónico válido.";
        }
        if (clave.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres.";
        }
        if (clave.getBytes(StandardCharsets.UTF_8).length > 72) {
            return "La contraseña es demasiado larga (máximo 72 caracteres).";
        }
        if (!clave.equals(clave2)) {
            return "Las contraseñas no coinciden.";
        }
        return null;
    }
}
