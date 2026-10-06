package com.example.mundial.modelo;

/** Las entidades con id incremental implementan esta interfaz (la usa GeneradorIdListener). */
public interface EntidadConId {
    Long getId();
    void setId(Long id);
}
