package com.example.mundial.controlador.web;

import com.example.mundial.repositorio.AsociacionRepositorio;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.CompeticionRepositorio;
import com.example.mundial.repositorio.EntrenadorRepositorio;
import com.example.mundial.repositorio.JugadorRepositorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Pagina de inicio (index.html) con el resumen. */
@Controller
public class InicioWeb {

    private final ClubRepositorio clubes;
    private final EntrenadorRepositorio entrenadores;
    private final JugadorRepositorio jugadores;
    private final AsociacionRepositorio asociaciones;
    private final CompeticionRepositorio competiciones;

    public InicioWeb(ClubRepositorio clubes, EntrenadorRepositorio entrenadores,
                     JugadorRepositorio jugadores, AsociacionRepositorio asociaciones,
                     CompeticionRepositorio competiciones) {
        this.clubes = clubes;
        this.entrenadores = entrenadores;
        this.jugadores = jugadores;
        this.asociaciones = asociaciones;
        this.competiciones = competiciones;
    }

    @GetMapping({"/", "/index"})
    public String inicio(Model model) {
        model.addAttribute("totalClubes", clubes.count());
        model.addAttribute("totalEntrenadores", entrenadores.count());
        model.addAttribute("totalJugadores", jugadores.count());
        model.addAttribute("totalAsociaciones", asociaciones.count());
        model.addAttribute("totalCompeticiones", competiciones.count());
        return "index";
    }
}
