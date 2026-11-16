
package com.example.airefresh.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String marca;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String modelo;

    @NotNull
    @Min(1000)
    @Column(nullable = false)
    private Integer btu;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 12, fraction = 2)
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal precio;

    @Column(length = 255)
    private String foto;

    @Column(length = 80)
    private String tipo;

    @DecimalMin("0.00")
    @Digits(integer = 12, fraction = 2)
    @Column(precision = 14, scale = 2)
    private BigDecimal precioAnterior;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer existencias = 0;

    @Column(columnDefinition = "bit(1) not null default 1")
    private boolean activo = true;

    @Column(columnDefinition = "bit(1) not null default 0")
    private boolean destacado = false;

    public Equipo() {
    }

    public int descuento() {
        if (precioAnterior == null
                || precio == null
                || precioAnterior.compareTo(precio) <= 0) {
            return 0;
        }

        return precioAnterior
                .subtract(precio)
                .multiply(BigDecimal.valueOf(100))
                .divide(precioAnterior, 0, RoundingMode.HALF_UP)
                .intValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getBtu() {
        return btu;
    }

    public void setBtu(Integer btu) {
        this.btu = btu;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecioAnterior() {
        return precioAnterior;
    }

    public void setPrecioAnterior(BigDecimal precioAnterior) {
        this.precioAnterior = precioAnterior;
    }

    public Integer getExistencias() {
        return existencias;
    }

    public void setExistencias(Integer existencias) {
        this.existencias = existencias;
    }

    public boolean isDestacado() {
        return destacado;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
