package com.billetera.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {
    private final String tipo;
    private final BigDecimal monto;
    private final LocalDateTime fecha;

    public Movimiento(String tipo, BigDecimal monto) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
    }

    public String getTipo() { return tipo; }
    public BigDecimal getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
