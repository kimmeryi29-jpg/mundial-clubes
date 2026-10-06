package com.example.mundial.servicio;

import com.example.mundial.modelo.Asociacion;
import com.example.mundial.modelo.Club;
import com.example.mundial.modelo.Competicion;
import com.example.mundial.modelo.Entrenador;
import com.example.mundial.modelo.Jugador;
import com.example.mundial.repositorio.AsociacionRepositorio;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.CompeticionRepositorio;
import com.example.mundial.repositorio.EntrenadorRepositorio;
import com.example.mundial.repositorio.JugadorRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/** Carga datos de ejemplo la primera vez (solo si no hay clubes). Personas inventadas. */
@Component
public class DatosIniciales implements CommandLineRunner {

    private final ClubRepositorio clubes;
    private final EntrenadorRepositorio entrenadores;
    private final JugadorRepositorio jugadores;
    private final AsociacionRepositorio asociaciones;
    private final CompeticionRepositorio competiciones;

    public DatosIniciales(ClubRepositorio clubes, EntrenadorRepositorio entrenadores,
                          JugadorRepositorio jugadores, AsociacionRepositorio asociaciones,
                          CompeticionRepositorio competiciones) {
        this.clubes = clubes;
        this.entrenadores = entrenadores;
        this.jugadores = jugadores;
        this.asociaciones = asociaciones;
        this.competiciones = competiciones;
    }

    @Override
    public void run(String... args) {
        if (clubes.count() > 0) return;

        Asociacion fcf = asociaciones.save(
                new Asociacion("Federación Colombiana de Fútbol", "Colombia", "Andrés Rojas"));

        Entrenador e1 = entrenadores.save(new Entrenador("Carlos", "Mejía", 52, "Colombiana"));
        Entrenador e2 = entrenadores.save(new Entrenador("Diego", "Fernández", 47, "Argentina"));
        entrenadores.save(new Entrenador("Luis", "Ortega", 58, "Uruguaya")); // libre

        Jugador j1 = jugadores.save(new Jugador("Juan", "Pérez", 1, "Portero"));
        Jugador j2 = jugadores.save(new Jugador("Mateo", "Gómez", 10, "Mediocampista"));
        Jugador j3 = jugadores.save(new Jugador("Santiago", "López", 9, "Delantero"));
        Jugador j4 = jugadores.save(new Jugador("Andrés", "Castro", 4, "Defensa"));
        jugadores.save(new Jugador("Felipe", "Ramírez", 7, "Extremo")); // sin club

        Competicion libertadores = competiciones.save(new Competicion("Copa Libertadores",
                23_000_000L, LocalDate.of(2026, 2, 4), LocalDate.of(2026, 11, 28)));
        Competicion liga = competiciones.save(new Competicion("Liga BetPlay",
                1_500_000L, LocalDate.of(2026, 1, 20), LocalDate.of(2026, 12, 15)));
        Competicion copa = competiciones.save(new Competicion("Copa Colombia",
                800_000L, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 10, 30)));

        Club millos = new Club();
        millos.setNombre("Millonarios");
        millos.setEntrenador(e1);
        millos.setAsociacion(fcf);
        millos.setJugadores(List.of(j1, j2));
        millos.setCompeticiones(List.of(libertadores, liga));
        clubes.save(millos);

        Club santaFe = new Club();
        santaFe.setNombre("Santa Fe");
        santaFe.setEntrenador(e2);
        santaFe.setAsociacion(fcf);
        santaFe.setJugadores(List.of(j3, j4));
        santaFe.setCompeticiones(List.of(liga, copa, libertadores));
        clubes.save(santaFe);
    }
}
