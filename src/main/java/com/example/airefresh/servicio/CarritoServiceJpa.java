package com.example.airefresh.servicio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.airefresh.controlador.dto.CarritoDTO;
import com.example.airefresh.controlador.dto.LineaCarritoDTO;
import com.example.airefresh.modelo.Equipo;

@Service
public class CarritoServiceJpa implements CarritoService {

    private final EquipoService equipos;

    public CarritoServiceJpa(EquipoService equipos) {
        this.equipos = equipos;
    }

    @Override
    public CarritoDTO resumen(Map<Long, Integer> carrito) {
        List<LineaCarritoDTO> lineas = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entrada : new ArrayList<>(carrito.entrySet())) {
            Optional<Equipo> encontrado = equipos.buscarPorId(entrada.getKey());

            if (encontrado.isEmpty() || !encontrado.get().isActivo()) {
                carrito.remove(entrada.getKey());
                continue;
            }

            Equipo equipo = encontrado.get();
            BigDecimal subtotal = equipo.getPrecio().multiply(BigDecimal.valueOf(entrada.getValue()));

            lineas.add(new LineaCarritoDTO(equipo, entrada.getValue(), subtotal));
            total = total.add(subtotal);
        }

        return new CarritoDTO(lineas, total);
    }

    @Override
    public void agregar(Map<Long, Integer> carrito, Long id, Integer cantidad) {
        Optional<Equipo> encontrado = equipos.buscarPorId(id);

        if (encontrado.isEmpty() || !encontrado.get().isActivo()) {
            throw new ReglaNegocioException("Ese producto ya no está disponible.");
        }
        if (cantidad == null || cantidad < 1 || cantidad > MAX_POR_PRODUCTO) {
            throw new ReglaNegocioException("Cantidad inválida.");
        }

        Equipo equipo = encontrado.get();
        int nueva = carrito.getOrDefault(id, 0) + cantidad;

        if (nueva > equipo.getExistencias() || nueva > MAX_POR_PRODUCTO) {
            throw new ReglaNegocioException(
                    "No hay existencias suficientes de " + equipo.getMarca() + " " + equipo.getModelo() + ".");
        }

        carrito.put(id, nueva);
    }

    @Override
    public void actualizar(Map<Long, Integer> carrito, Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            carrito.remove(id);
            return;
        }

        Optional<Equipo> encontrado = equipos.buscarPorId(id);

        if (encontrado.isEmpty() || !encontrado.get().isActivo()) {
            carrito.remove(id);
            throw new ReglaNegocioException("Ese producto ya no está disponible.");
        }
        if (cantidad > MAX_POR_PRODUCTO || cantidad > encontrado.get().getExistencias()) {
            throw new ReglaNegocioException("La cantidad supera las existencias disponibles.");
        }

        carrito.put(id, cantidad);
    }

    @Override
    public void quitar(Map<Long, Integer> carrito, Long id) {
        carrito.remove(id);
    }
}
