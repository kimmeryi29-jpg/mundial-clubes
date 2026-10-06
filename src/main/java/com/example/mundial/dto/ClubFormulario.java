package com.example.mundial.dto;

import com.example.mundial.modelo.Club;
import com.example.mundial.modelo.Competicion;
import com.example.mundial.modelo.Jugador;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Lo que llega del formulario (o del JSON de la API) para crear/editar un club.
 * Solo trae los ids de los objetos relacionados; ClubServicio los convierte en objetos.
 * Ejemplo JSON:
 * { "nombre": "Millonarios", "entrenadorId": 1, "asociacionId": 1,
 *   "jugadoresIds": [1, 2], "competicionesIds": [1] }
 */
public class ClubFormulario {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private Long entrenadorId;
    private Long asociacionId;
    private List<Long> jugadoresIds = new ArrayList<>();
    private List<Long> competicionesIds = new ArrayList<>();

    public ClubFormulario() { }

    /** Llena el formulario con los datos de un club existente (para editar). */
    public static ClubFormulario desde(Club club) {
        ClubFormulario f = new ClubFormulario();
        f.setId(club.getId());
        f.setNombre(club.getNombre());
        f.setEntrenadorId(club.getEntrenador() != null ? club.getEntrenador().getId() : null);
        f.setAsociacionId(club.getAsociacion() != null ? club.getAsociacion().getId() : null);
        f.setJugadoresIds(club.getJugadores().stream().map(Jugador::getId).toList());
        f.setCompeticionesIds(club.getCompeticiones().stream().map(Competicion::getId).toList());
        return f;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getEntrenadorId() { return entrenadorId; }
    public void setEntrenadorId(Long entrenadorId) { this.entrenadorId = entrenadorId; }
    public Long getAsociacionId() { return asociacionId; }
    public void setAsociacionId(Long asociacionId) { this.asociacionId = asociacionId; }
    public List<Long> getJugadoresIds() { return jugadoresIds; }
    public void setJugadoresIds(List<Long> jugadoresIds) {
        this.jugadoresIds = jugadoresIds != null ? new ArrayList<>(jugadoresIds) : new ArrayList<>();
    }
    public List<Long> getCompeticionesIds() { return competicionesIds; }
    public void setCompeticionesIds(List<Long> competicionesIds) {
        this.competicionesIds = competicionesIds != null ? new ArrayList<>(competicionesIds) : new ArrayList<>();
    }
}
