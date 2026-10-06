package com.example.mundial.modelo;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Diapositiva 18: Asociacion (ej. FCF - Federacion Colombiana de Futbol). */
@Document(collection = "asociaciones")
public class Asociacion implements EntidadConId {

    @Id
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El pais es obligatorio")
    private String pais;

    @NotBlank(message = "El presidente es obligatorio")
    private String presidente;

    public Asociacion() { }

    public Asociacion(String nombre, String pais, String presidente) {
        this.nombre = nombre;
        this.pais = pais;
        this.presidente = presidente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getPresidente() { return presidente; }
    public void setPresidente(String presidente) { this.presidente = presidente; }
}
