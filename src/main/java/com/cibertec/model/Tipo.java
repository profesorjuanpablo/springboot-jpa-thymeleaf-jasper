package com.cibertec.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_tipos")
public class Tipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo")
    private Integer idTipo;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    public Tipo() {
    }
    
    public Integer getIdTipo() {
        return idTipo;
    }
    
    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}