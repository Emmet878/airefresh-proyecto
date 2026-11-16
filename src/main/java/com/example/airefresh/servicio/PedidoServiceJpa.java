package com.example.airefresh.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airefresh.modelo.Equipo;
import com.example.airefresh.modelo.Pedido;
import com.example.airefresh.modelo.PedidoItem;
import com.example.airefresh.modelo.Usuario;
import com.example.airefresh.repositorio.EquipoRepository;
import com.example.airefresh.repositorio.PedidoRepository;

@Service
public class PedidoServiceJpa implements PedidoService {

    private static final List<String> METODOS_PAGO = List.of("CONTRAENTREGA", "TRANSFERENCIA");

    private static final List<String> ESTADOS = List.of(
            "PENDIENTE", "CONFIRMADO", "EN_PREPARACION", "ENVIADO", "ENTREGADO", "CANCELADO");

    private final PedidoRepository pedidos;
    private final EquipoRepository equipos;

    public PedidoServiceJpa(PedidoRepository pedidos, EquipoRepository equipos) {
        this.pedidos = pedidos;
        this.equipos = equipos;
    }

    @Override
    public List<String> estados() {
        return ESTADOS;
    }

    @Override
    @Transactional
    public Pedido crear(Usuario usuario, Map<Long, Integer> carrito, String metodo,
                        String direccion, String telefono, String notas) {

        if (carrito == null || carrito.isEmpty()) {
            throw new ReglaNegocioException("El carrito está vacío.");
        }
        if (direccion == null || direccion.isBlank() || direccion.trim().length() > 180) {
            throw new ReglaNegocioException("Escribe una dirección de entrega válida.");
        }
        if (telefono == null || telefono.isBlank() || telefono.trim().length() > 40) {
            throw new ReglaNegocioException("Escribe un teléfono de contacto válido.");
        }
        if (notas != null && notas.length() > 1000) {
            throw new ReglaNegocioException("Las notas superan el límite permitido.");
        }
        if (!METODOS_PAGO.contains(metodo)) {
            throw new ReglaNegocioException("Método de pago inválido.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setMetodoPago(metodo);
        pedido.setDireccion(direccion.trim());
        pedido.setTelefono(telefono.trim());
        pedido.setNotas(notas == null || notas.isBlank() ? null : notas.trim());

        BigDecimal total = BigDecimal.ZERO;

        // Orden estable por id para evitar bloqueos cruzados entre pedidos simultaneos.
        for (Map.Entry<Long, Integer> linea : new TreeMap<>(carrito).entrySet()) {
            int cantidad = linea.getValue();

            if (cantidad < 1 || cantidad > CarritoService.MAX_POR_PRODUCTO) {
                throw new ReglaNegocioException("Cantidad inválida.");
            }

            Equipo equipo = equipos.buscarParaActualizar(linea.getKey())
                    .orElseThrow(() -> new ReglaNegocioException("Uno de los productos ya no existe."));

            if (!equipo.isActivo() || equipo.getExistencias() < cantidad) {
                throw new ReglaNegocioException(
                        "No hay existencias suficientes de " + equipo.getMarca() + " " + equipo.getModelo());
            }

            PedidoItem item = new PedidoItem();
            item.setEquipo(equipo);
            item.setDescripcion(equipo.getMarca() + " " + equipo.getModelo());
            item.setCantidad(cantidad);
            item.setPrecioUnitario(equipo.getPrecio());
            pedido.agregarItem(item);

            total = total.add(equipo.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
            equipo.setExistencias(equipo.getExistencias() - cantidad);
        }

        pedido.setTotal(total);
        return pedidos.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> paraUsuario(Long usuarioId) {
        return pedidos.findByUsuarioIdOrderByCreadoEnDesc(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarDeUsuario(Long pedidoId, Long usuarioId) {
        return pedidos.findByIdAndUsuarioId(pedidoId, usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> todos() {
        return pedidos.findAllByOrderByCreadoEnDesc();
    }

    @Override
    @Transactional
    public void actualizarEstado(Long id, String estado) {
        if (!ESTADOS.contains(estado)) {
            throw new ReglaNegocioException("Estado inválido.");
        }

        Pedido pedido = pedidos.findById(id)
                .orElseThrow(() -> new NoEncontradoException("Pedido no encontrado"));

        boolean estabaCancelado = "CANCELADO".equals(pedido.getEstado());
        boolean seCancela = "CANCELADO".equals(estado);

        if (estabaCancelado && !seCancela) {
            throw new ReglaNegocioException("Un pedido cancelado no se puede reabrir; crea un pedido nuevo.");
        }

        if (seCancela && !estabaCancelado) {
            for (PedidoItem item : pedido.getItems()) {
                if (item.getEquipo() != null) {
                    equipos.buscarParaActualizar(item.getEquipo().getId())
                            .ifPresent(e -> e.setExistencias(e.getExistencias() + item.getCantidad()));
                }
            }
        }

        pedido.setEstado(estado);
        pedidos.save(pedido);
    }
}
