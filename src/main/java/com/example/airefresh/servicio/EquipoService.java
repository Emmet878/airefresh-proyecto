package com.example.airefresh.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.airefresh.modelo.Equipo;

public interface EquipoService {

    /** Todos los equipos, también los desactivados (para el panel de administración). */
    List<Equipo> listar();

    /** Solo los activos, del más nuevo al más antiguo (catálogo y API). */
    List<Equipo> listarActivos();

    /** Hasta 4 destacados para el inicio; si no hay ninguno marcado, los 4 más recientes. */
    List<Equipo> destacados();

    List<Equipo> buscarPorMarca(String marca);

    /** Filtros del catálogo; cualquier filtro {@code null} o vacío no se aplica. */
    List<Equipo> filtrar(List<Integer> btu, List<String> tipo, List<String> marca,
                         BigDecimal min, BigDecimal max);

    Optional<Equipo> buscarPorId(Long id);

    long contar();

    Equipo crear(Equipo equipo);

    /** @throws NoEncontradoException si no existe */
    Equipo editar(Long id, Equipo equipo);

    /** Borrado lógico: lo marca como inactivo. @throws NoEncontradoException si no existe */
    void borrar(Long id);
}
