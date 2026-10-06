package com.example.mundial.controlador.api;

import com.example.mundial.modelo.Competicion;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.CompeticionRepositorio;
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

/** API REST en /api/competiciones */
@RestController
@RequestMapping("/api/competiciones")
public class CompeticionApiRest {

    private final CompeticionRepositorio repositorio;
    private final ClubRepositorio clubes;

    public CompeticionApiRest(CompeticionRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    @GetMapping
    public List<Competicion> listar() {
        return repositorio.findAll(Sort.by("fechaInicio"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Competicion> buscar(@PathVariable Long id) {
        return repositorio.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Competicion> crear(@Valid @RequestBody Competicion competicion) {
        competicion.setId(null); // el id lo genera el sistema
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(competicion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Competicion> actualizar(@PathVariable Long id, @Valid @RequestBody Competicion competicion) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        competicion.setId(id);
        return ResponseEntity.ok(repositorio.save(competicion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (clubes.usaCompeticion(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La competición está asignado a un club");
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
