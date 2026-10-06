package com.example.mundial.controlador.web;

import com.example.mundial.modelo.Jugador;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.JugadorRepositorio;
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

/** Pagina jugadores.html: formulario a la izquierda y lista a la derecha. */
@Controller
@RequestMapping("/jugadores")
public class JugadorWeb {

    private final JugadorRepositorio repositorio;
    private final ClubRepositorio clubes;

    public JugadorWeb(JugadorRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    private String mostrar(Jugador jugador, Model model) {
        model.addAttribute("jugador", jugador);
        model.addAttribute("lista", repositorio.findAll(Sort.by("numero")));
        return "jugadores";
    }

    // Listar + formulario vacio
    @GetMapping
    public String listar(Model model) {
        return mostrar(new Jugador(), model);
    }

    // Crear o actualizar
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("jugador") Jugador jugador, BindingResult errores,
                          Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            return mostrar(jugador, model);
        }
        boolean nuevo = jugador.getId() == null;
        repositorio.save(jugador);
        flash.addFlashAttribute("mensaje", nuevo ? "Registro guardado" : "Registro actualizado");
        return "redirect:/jugadores";
    }

    // Cargar en el formulario para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return repositorio.findById(id)
                .map(encontrado -> mostrar(encontrado, model))
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "No existe el registro " + id);
                    return "redirect:/jugadores";
                });
    }

    // Eliminar (no deja borrar si un club lo usa: "no action" de la foreign key)
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (clubes.usaJugador(id)) {
            flash.addFlashAttribute("error", "El jugador está asignado a un club. Quítalo del club primero.");
        } else {
            repositorio.deleteById(id);
            flash.addFlashAttribute("mensaje", "Registro eliminado");
        }
        return "redirect:/jugadores";
    }
}
