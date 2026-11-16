package com.example.airefresh.servicio;

import java.util.Map;

import com.example.airefresh.controlador.dto.CarritoDTO;

/**
 * Reglas del carrito. El carrito es un mapa id de equipo -> cantidad que guarda
 * la sesión web; este servicio lo valida y lo modifica, pero no lo guarda.
 */
public interface CarritoService {

    int MAX_POR_PRODUCTO = 20;

    /** Arma las líneas y el total; quita del mapa los productos que ya no están disponibles. */
    CarritoDTO resumen(Map<Long, Integer> carrito);

    /** @throws ReglaNegocioException si el producto no está disponible, la cantidad es inválida o no hay stock */
    void agregar(Map<Long, Integer> carrito, Long id, Integer cantidad);

    /**
     * Fija la cantidad (0 o menos lo quita).
     *
     * @throws ReglaNegocioException si el producto ya no está disponible o supera las existencias
     */
    void actualizar(Map<Long, Integer> carrito, Long id, Integer cantidad);

    void quitar(Map<Long, Integer> carrito, Long id);
}
