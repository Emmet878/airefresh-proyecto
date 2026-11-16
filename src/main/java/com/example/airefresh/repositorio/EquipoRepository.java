package com.example.airefresh.repositorio;

import com.example.airefresh.modelo.Equipo;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findByActivoTrueOrderByIdDesc();

    List<Equipo> findTop4ByActivoTrueOrderByIdDesc();

    List<Equipo> findTop4ByActivoTrueAndDestacadoTrueOrderByIdDesc();

    List<Equipo> findByMarcaContainingIgnoreCaseAndActivoTrue(String marca);

    /** Bloquea la fila para que dos pedidos simultaneos no vendan el mismo stock. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Equipo e where e.id = :id")
    Optional<Equipo> buscarParaActualizar(@Param("id") Long id);
}
