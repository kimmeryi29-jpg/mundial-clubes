package com.example.mundial.controlador.web;

import com.example.mundial.modelo.Asociacion;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.AsociacionRepositorio;
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

/** Pagina asociaciones.html: formulario a la izquierda y lista a la derecha. */
@Controller
@RequestMapping("/asociaciones")
public class AsociacionWeb {

    private final AsociacionRepositorio repositorio;
    private final ClubRepositorio clubes;

    public AsociacionWeb(AsociacionRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    private String mostrar(Asociacion asociacion, Model model) {
        model.addAttribute("asociacion", asociacion);
        model.addAttribute("lista", repositorio.findAll(Sort.by("nombre")));
        return "asociaciones";
    }

    // Listar + formulario vacio
    @GetMapping
    public String listar(Model model) {
        return mostrar(new Asociacion(), model);
    }

    // Crear o actualizar
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("asociacion") Asociacion asociacion, BindingResult errores,
                          Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            return mostrar(asociacion, model);
        }
        boolean nuevo = asociacion.getId() == null;
        repositorio.save(asociacion);
        flash.addFlashAttribute("mensaje", nuevo ? "Registro guardado" : "Registro actualizado");
        return "redirect:/asociaciones";
    }

    // Cargar en el formulario para editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        return repositorio.findById(id)
                .map(encontrado -> mostrar(encontrado, model))
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "No existe el registro " + id);
                    return "redirect:/asociaciones";
                });
    }

    // Eliminar (no deja borrar si un club lo usa: "no action" de la foreign key)
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (clubes.usaAsociacion(id)) {
            flash.addFlashAttribute("error", "La asociación está asignado a un club. Quítalo del club primero.");
        } else {
            repositorio.deleteById(id);
            flash.addFlashAttribute("mensaje", "Registro eliminado");
        }
        return "redirect:/asociaciones";
    }
}
