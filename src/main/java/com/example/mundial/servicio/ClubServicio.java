package com.example.mundial.servicio;

import com.example.mundial.dto.ClubFormulario;
import com.example.mundial.modelo.Club;
import com.example.mundial.modelo.Entrenador;
import com.example.mundial.modelo.Jugador;
import com.example.mundial.repositorio.AsociacionRepositorio;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.CompeticionRepositorio;
import com.example.mundial.repositorio.EntrenadorRepositorio;
import com.example.mundial.repositorio.JugadorRepositorio;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Logica de las relaciones del Club. La usan el controlador web y la API REST. */
@Service
public class ClubServicio {

    private final ClubRepositorio clubes;
    private final EntrenadorRepositorio entrenadores;
    private final JugadorRepositorio jugadores;
    private final AsociacionRepositorio asociaciones;
    private final CompeticionRepositorio competiciones;

    public ClubServicio(ClubRepositorio clubes, EntrenadorRepositorio entrenadores,
                        JugadorRepositorio jugadores, AsociacionRepositorio asociaciones,
                        CompeticionRepositorio competiciones) {
        this.clubes = clubes;
        this.entrenadores = entrenadores;
        this.jugadores = jugadores;
        this.asociaciones = asociaciones;
        this.competiciones = competiciones;
    }

    /**
     * Revisa las reglas de las relaciones. Devuelve la lista de errores (vacia si todo esta bien).
     * - @OneToOne: el entrenador no puede estar en otro club.
     * - @OneToMany: un jugador no puede estar en otro club.
     */
    public List<String> validar(ClubFormulario f) {
        List<String> errores = new ArrayList<>();
        for (Club otro : clubes.findAll()) {
            if (Objects.equals(otro.getId(), f.getId())) continue;

            if (f.getEntrenadorId() != null && otro.getEntrenador() != null
                    && f.getEntrenadorId().equals(otro.getEntrenador().getId())) {
                errores.add("El entrenador ya dirige a " + otro.getNombre() + " (relacion uno a uno)");
            }
            for (Jugador j : otro.getJugadores()) {
                if (f.getJugadoresIds().contains(j.getId())) {
                    errores.add(j.getNombreCompleto() + " ya juega en " + otro.getNombre()
                            + " (relacion uno a muchos)");
                }
            }
        }
        if (f.getEntrenadorId() != null && !entrenadores.existsById(f.getEntrenadorId())) {
            errores.add("No existe el entrenador con id " + f.getEntrenadorId());
        }
        if (f.getAsociacionId() != null && !asociaciones.existsById(f.getAsociacionId())) {
            errores.add("No existe la asociacion con id " + f.getAsociacionId());
        }
        return errores;
    }

    /** Convierte el formulario (ids) en un Club con los objetos relacionados y lo guarda. */
    public Club guardar(ClubFormulario f) {
        Club club = new Club();
        club.setId(f.getId());
        club.setNombre(f.getNombre());
        club.setEntrenador(f.getEntrenadorId() == null ? null
                : entrenadores.findById(f.getEntrenadorId()).orElse(null));
        club.setAsociacion(f.getAsociacionId() == null ? null
                : asociaciones.findById(f.getAsociacionId()).orElse(null));
        club.setJugadores(new ArrayList<>(jugadores.findAllById(f.getJugadoresIds())));
        club.setCompeticiones(new ArrayList<>(competiciones.findAllById(f.getCompeticionesIds())));
        return clubes.save(club);
    }

    /** Entrenadores libres + el del club que se esta editando. */
    public List<Entrenador> entrenadoresDisponibles(Long clubId) {
        Set<Long> ocupados = new HashSet<>();
        for (Club c : clubes.findAll()) {
            if (!Objects.equals(c.getId(), clubId) && c.getEntrenador() != null) {
                ocupados.add(c.getEntrenador().getId());
            }
        }
        return entrenadores.findAll(Sort.by("id")).stream()
                .filter(e -> !ocupados.contains(e.getId())).toList();
    }

    /** Jugadores sin club + los del club que se esta editando. */
    public List<Jugador> jugadoresDisponibles(Long clubId) {
        Set<Long> ocupados = new HashSet<>();
        for (Club c : clubes.findAll()) {
            if (!Objects.equals(c.getId(), clubId)) {
                c.getJugadores().forEach(j -> ocupados.add(j.getId()));
            }
        }
        return jugadores.findAll(Sort.by("id")).stream()
                .filter(j -> !ocupados.contains(j.getId())).toList();
    }
}
