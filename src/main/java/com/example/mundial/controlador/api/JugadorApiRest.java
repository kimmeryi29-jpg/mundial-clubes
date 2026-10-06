package com.example.mundial.controlador.api;

import com.example.mundial.modelo.Jugador;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.repositorio.JugadorRepositorio;
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

/** API REST en /api/jugadores */
@RestController
@RequestMapping("/api/jugadores")
public class JugadorApiRest {

    private final JugadorRepositorio repositorio;
    private final ClubRepositorio clubes;

    public JugadorApiRest(JugadorRepositorio repositorio, ClubRepositorio clubes) {
        this.repositorio = repositorio;
        this.clubes = clubes;
    }

    @GetMapping
    public List<Jugador> listar() {
        return repositorio.findAll(Sort.by("numero"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jugador> buscar(@PathVariable Long id) {
        return repositorio.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Jugador> crear(@Valid @RequestBody Jugador jugador) {
        jugador.setId(null); // el id lo genera el sistema
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(jugador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizar(@PathVariable Long id, @Valid @RequestBody Jugador jugador) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        jugador.setId(id);
        return ResponseEntity.ok(repositorio.save(jugador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (clubes.usaJugador(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El jugador está asignado a un club");
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
