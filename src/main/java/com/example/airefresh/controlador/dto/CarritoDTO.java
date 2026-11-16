package com.example.airefresh.controlador.dto;

import java.math.BigDecimal;
import java.util.List;

/** El carrito listo para mostrar: sus líneas y el total. */
public class CarritoDTO {

    private final List<LineaCarritoDTO> lineas;
    private final BigDecimal total;

    public CarritoDTO(List<LineaCarritoDTO> lineas, BigDecimal total) {
        this.lineas = lineas;
        this.total = total;
    }

    public List<LineaCarritoDTO> getLineas() {
        return lineas;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
