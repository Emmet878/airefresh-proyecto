package com.example.airefresh.servicio;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.airefresh.modelo.Pedido;
import com.example.airefresh.modelo.Usuario;

public interface PedidoService {

    /** Estados que puede tener un pedido, en orden de avance. */
    List<String> estados();

    /**
     * Crea el pedido y descuenta el stock.
     *
     * @param carrito id de equipo -> cantidad
     * @throws ReglaNegocioException carrito vacío, datos inválidos o sin existencias suficientes
     */
    Pedido crear(Usuario usuario, Map<Long, Integer> carrito, String metodo,
                 String direccion, String telefono, String notas);

    List<Pedido> paraUsuario(Long usuarioId);

    Optional<Pedido> buscarDeUsuario(Long pedidoId, Long usuarioId);

    List<Pedido> todos();

    /**
     * Cambia el estado; al cancelar devuelve el stock.
     *
     * @throws NoEncontradoException si el pedido no existe
     * @throws ReglaNegocioException si el estado no es válido o se reabre un pedido cancelado
     */
    void actualizarEstado(Long id, String estado);
}
