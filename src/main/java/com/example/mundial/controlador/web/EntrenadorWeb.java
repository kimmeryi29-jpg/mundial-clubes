package com.example.mundial.controlador.web;

import com.example.mundial.modelo.Entrenador;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.EntrenadorRepositorio;
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

/** Pagina entrenadores.html: formulario a la izquierda y lista a la derecha. */
@Controller
@RequestMapping("/entrenadores")
public class EntrenadorWeb {

    private final EntrenadorRepositorio repositorio;
    private final ClubRepositorio clubes;

    public EntrenadorWeb(EntrenadorRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    private String mostrar(Entrenador entrenador, Model model) {
        model.addAttribute("entrenador", entrenador);
        model.addAttribute("lista", repositorio.findAll(Sort.by("apellido")));
        return "entrenadores";
    }

    // Listar + formulario vacio
    @GetMapping
    public String listar(Model model) {
        return mostrar(new Entrenador(), model);
    }

    // Crear o actualizar
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("entrenador") Entrenador entrenador, BindingResult errores,
                          Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            return mostrar(entrenador, model);
        }
        boolean nuevo = entrenador.getId() == null;
        repositorio.save(entrenador);
        flash.addFlashAttribute("mensaje", nuevo ? "Registro guardado" : "Registro actualizado");
        return "redirect:/entrenadores";
    }

    // Cargar en el formulario para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return repositorio.findById(id)
                .map(encontrado -> mostrar(encontrado, model))
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "No existe el registro " + id);
                    return "redirect:/entrenadores";
                });
    }

    // Eliminar (no deja borrar si un club lo usa: "no action" de la foreign key)
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (clubes.usaEntrenador(id)) {
            flash.addFlashAttribute("error", "El entrenador está asignado a un club. Quítalo del club primero.");
        } else {
            repositorio.deleteById(id);
            flash.addFlashAttribute("mensaje", "Registro eliminado");
        }
        return "redirect:/entrenadores";
    }
}
