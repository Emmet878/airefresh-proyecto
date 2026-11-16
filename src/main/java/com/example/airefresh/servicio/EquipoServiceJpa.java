package com.example.airefresh.servicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airefresh.modelo.Equipo;
import com.example.airefresh.repositorio.EquipoRepository;

@Service
@Transactional
public class EquipoServiceJpa implements EquipoService {

    private final EquipoRepository repo;

    public EquipoServiceJpa(EquipoRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> listar() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> listarActivos() {
        return repo.findByActivoTrueOrderByIdDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> destacados() {
        List<Equipo> destacados = repo.findTop4ByActivoTrueAndDestacadoTrueOrderByIdDesc();
        return destacados.isEmpty() ? repo.findTop4ByActivoTrueOrderByIdDesc() : destacados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> buscarPorMarca(String marca) {
        return repo.findByMarcaContainingIgnoreCaseAndActivoTrue(marca);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipo> filtrar(List<Integer> btu, List<String> tipo, List<String> marca,
                                BigDecimal min, BigDecimal max) {
        return repo.findByActivoTrueOrderByIdDesc().stream()
                .filter(e -> btu == null || btu.isEmpty() || btu.contains(e.getBtu()))
                .filter(e -> tipo == null || tipo.isEmpty() || tipo.contains(e.getTipo()))
                .filter(e -> marca == null || marca.stream().allMatch(String::isBlank)
                        || marca.stream().anyMatch(m -> !m.isBlank()
                                && e.getMarca().toLowerCase().contains(m.trim().toLowerCase())))
                .filter(e -> min == null || e.getPrecio().compareTo(min) >= 0)
                .filter(e -> max == null || e.getPrecio().compareTo(max) <= 0)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contar() {
        return repo.count();
    }

    @Override
    public Equipo crear(Equipo equipo) {
        equipo.setId(null);
        return repo.save(equipo);
    }

    @Override
    public Equipo editar(Long id, Equipo equipo) {
        Equipo actual = repo.findById(id)
                .orElseThrow(() -> new NoEncontradoException("Equipo no encontrado"));

        actual.setMarca(equipo.getMarca());
        actual.setModelo(equipo.getModelo());
        actual.setBtu(equipo.getBtu());
        actual.setPrecio(equipo.getPrecio());
        actual.setPrecioAnterior(equipo.getPrecioAnterior());
        actual.setTipo(equipo.getTipo());
        actual.setFoto(equipo.getFoto());
        actual.setExistencias(equipo.getExistencias());
        actual.setActivo(equipo.isActivo());
        actual.setDestacado(equipo.isDestacado());

        return repo.save(actual);
    }

    @Override
    public void borrar(Long id) {
        Equipo equipo = repo.findById(id)
                .orElseThrow(() -> new NoEncontradoException("Equipo no encontrado"));
        equipo.setActivo(false);
        repo.save(equipo);
    }
}
