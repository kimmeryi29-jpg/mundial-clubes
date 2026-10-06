package com.example.mundial.controlador.web;

import com.example.mundial.dto.ClubFormulario;
import com.example.mundial.repositorio.AsociacionRepositorio;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.CompeticionRepositorio;
import com.example.mundial.servicio.ClubServicio;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/** Pagina clubes.html (formulario con todas las relaciones) y club-detalle.html */
@Controller
@RequestMapping("/clubes")
public class ClubWeb {

    private final ClubRepositorio clubes;
    private final AsociacionRepositorio asociaciones;
    private final CompeticionRepositorio competiciones;
    private final ClubServicio servicio;

    public ClubWeb(ClubRepositorio clubes, AsociacionRepositorio asociaciones,
                   CompeticionRepositorio competiciones, ClubServicio servicio) {
        this.clubes = clubes;
        this.asociaciones = asociaciones;
        this.competiciones = competiciones;
        this.servicio = servicio;
    }

    private String mostrar(ClubFormulario club, Model model) {
        model.addAttribute("club", club);
        model.addAttribute("lista", clubes.findAll(Sort.by("nombre")));
        model.addAttribute("entrenadores", servicio.entrenadoresDisponibles(club.getId()));
        model.addAttribute("jugadores", servicio.jugadoresDisponibles(club.getId()));
        model.addAttribute("asociaciones", asociaciones.findAll(Sort.by("nombre")));
        model.addAttribute("competiciones", competiciones.findAll(Sort.by("fechaInicio")));
        return "clubes";
    }

    @GetMapping
    public String listar(Model model) {
        return mostrar(new ClubFormulario(), model);
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("club") ClubFormulario club, BindingResult errores,
                          Model model, RedirectAttributes flash) {
        List<String> reglas = servicio.validar(club);
        reglas.forEach(r -> errores.reject("relacion", r));
        if (errores.hasErrors()) {
            return mostrar(club, model);
        }
        boolean nuevo = club.getId() == null;
        servicio.guardar(club);
        flash.addFlashAttribute("mensaje", nuevo ? "Club guardado" : "Club actualizado");
        return "redirect:/clubes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return clubes.findById(id)
                .map(c -> mostrar(ClubFormulario.desde(c), model))
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "No existe el club " + id);
                    return "redirect:/clubes";
                });
    }

    // Ver el club con todas sus relaciones
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return clubes.findById(id)
                .map(c -> {
                    model.addAttribute("club", c);
                    return "club-detalle";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "No existe el club " + id);
                    return "redirect:/clubes";
                });
    }

    // Borrar un club solo borra el club; sus jugadores y entrenador quedan libres
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        clubes.deleteById(id);
        flash.addFlashAttribute("mensaje", "Club eliminado");
        return "redirect:/clubes";
    }
}
