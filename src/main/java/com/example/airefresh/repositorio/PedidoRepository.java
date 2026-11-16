package com.example.airefresh.repositorio;

import com.example.airefresh.modelo.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdOrderByCreadoEnDesc(Long usuarioId);

    List<Pedido> findAllByOrderByCreadoEnDesc();

    Optional<Pedido> findByIdAndUsuarioId(Long id, Long usuarioId);
}
