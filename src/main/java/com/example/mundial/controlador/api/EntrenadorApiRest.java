package com.example.mundial.controlador.api;

import com.example.mundial.modelo.Entrenador;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.EntrenadorRepositorio;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** API REST en /api/entrenadores */
@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorApiRest {

    private final EntrenadorRepositorio repositorio;
    private final ClubRepositorio clubes;

    public EntrenadorApiRest(EntrenadorRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    @GetMapping
    public List<Entrenador> listar() {
        return repositorio.findAll(Sort.by("apellido"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenador> buscar(@PathVariable Long id) {
        return repositorio.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Entrenador> crear(@Valid @RequestBody Entrenador entrenador) {
        entrenador.setId(null); // el id lo genera el sistema
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(entrenador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenador> actualizar(@PathVariable Long id, @Valid @RequestBody Entrenador entrenador) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        entrenador.setId(id);
        return ResponseEntity.ok(repositorio.save(entrenador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (clubes.usaEntrenador(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El entrenador está asignado a un club");
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
