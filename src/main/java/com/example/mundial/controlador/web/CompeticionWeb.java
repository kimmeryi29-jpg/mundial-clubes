package com.example.mundial.controlador.web;

import com.example.mundial.modelo.Competicion;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.CompeticionRepositorio;
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

/** Pagina competiciones.html: formulario a la izquierda y lista a la derecha. */
@Controller
@RequestMapping("/competiciones")
public class CompeticionWeb {

    private final CompeticionRepositorio repositorio;
    private final ClubRepositorio clubes;

    public CompeticionWeb(CompeticionRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    private String mostrar(Competicion competicion, Model model) {
        model.addAttribute("competicion", competicion);
        model.addAttribute("lista", repositorio.findAll(Sort.by("fechaInicio")));
        return "competiciones";
    }

    // Listar + formulario vacio
    @GetMapping
    public String listar(Model model) {
        return mostrar(new Competicion(), model);
    }

    // Crear o actualizar
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("competicion") Competicion competicion, BindingResult errores,
                          Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            return mostrar(competicion, model);
        }
        boolean nuevo = competicion.getId() == null;
        repositorio.save(competicion);
        flash.addFlashAttribute("mensaje", nuevo ? "Registro guardado" : "Registro actualizado");
        return "redirect:/competiciones";
    }

    // Cargar en el formulario para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return repositorio.findById(id)
                .map(encontrado -> mostrar(encontrado, model))
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "No existe el registro " + id);
                    return "redirect:/competiciones";
                });
    }

    // Eliminar (no deja borrar si un club lo usa: "no action" de la foreign key)
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (clubes.usaCompeticion(id)) {
            flash.addFlashAttribute("error", "La competición está asignado a un club. Quítalo del club primero.");
        } else {
            repositorio.deleteById(id);
            flash.addFlashAttribute("mensaje", "Registro eliminado");
        }
        return "redirect:/competiciones";
    }
}
