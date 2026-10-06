package com.example.mundial.controlador.api;

import com.example.mundial.modelo.Asociacion;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.AsociacionRepositorio;
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

/** API REST en /api/asociaciones */
@RestController
@RequestMapping("/api/asociaciones")
public class AsociacionApiRest {

    private final AsociacionRepositorio repositorio;
    private final ClubRepositorio clubes;

    public AsociacionApiRest(AsociacionRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    @GetMapping
    public List<Asociacion> listar() {
        return repositorio.findAll(Sort.by("nombre"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asociacion> buscar(@PathVariable Long id) {
        return repositorio.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Asociacion> crear(@Valid @RequestBody Asociacion asociacion) {
        asociacion.setId(null); // el id lo genera el sistema
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(asociacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asociacion> actualizar(@PathVariable Long id, @Valid @RequestBody Asociacion asociacion) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        asociacion.setId(id);
        return ResponseEntity.ok(repositorio.save(asociacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (clubes.usaAsociacion(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La asociación está asignado a un club");
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
