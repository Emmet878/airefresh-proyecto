package com.example.airefresh.controlador.dto;

import java.math.BigDecimal;

import com.example.airefresh.modelo.Equipo;

/** Una línea del carrito tal como la pinta la vista: equipo, cantidad y subtotal. */
public class LineaCarritoDTO {

    private final Equipo equipo;
    private final int cantidad;
    private final BigDecimal subtotal;

    public LineaCarritoDTO(Equipo equipo, int cantidad, BigDecimal subtotal) {
        this.equipo = equipo;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
