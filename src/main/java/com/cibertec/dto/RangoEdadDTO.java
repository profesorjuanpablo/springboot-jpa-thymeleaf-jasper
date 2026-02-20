package com.cibertec.dto;

public class RangoEdadDTO {
    private String rango;
    private Long cantidad;

    public RangoEdadDTO(String rango, Long cantidad) {
        this.rango = rango;
        this.cantidad = cantidad;
    }

    public String getRango() { return rango; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
    public Long getCantidad() { return cantidad; }
    public void setRango(String rango) { this.rango = rango; }
}