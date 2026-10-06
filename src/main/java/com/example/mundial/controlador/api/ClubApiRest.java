package com.example.mundial.controlador.api;

import com.example.mundial.dto.ClubFormulario;
import com.example.mundial.modelo.Club;
import com.example.mundial.repositorio.ClubRepositorio;
import com.example.mundial.servicio.ClubServicio;
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

/**
 * API REST en /api/clubes.
 * GET devuelve el club con sus objetos relacionados completos.
 * POST/PUT reciben solo los ids:
 * { "nombre": "Nacional", "entrenadorId": 3, "asociacionId": 1,
 *   "jugadoresIds": [5], "competicionesIds": [1, 2] }
 */
@RestController
@RequestMapping("/api/clubes")
public class ClubApiRest {

    private final ClubRepositorio clubes;
    private final ClubServicio servicio;

    public ClubApiRest(ClubRepositorio clubes, ClubServicio servicio) {
        this.clubes = clubes;
        this.servicio = servicio;
    }

    @GetMapping
    public List<Club> listar() {
        return clubes.findAll(Sort.by("nombre"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> buscar(@PathVariable Long id) {
        return clubes.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ClubFormulario datos) {
        datos.setId(null);
        List<String> errores = servicio.validar(datos);
        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.guardar(datos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ClubFormulario datos) {
        if (!clubes.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        datos.setId(id);
        List<String> errores = servicio.validar(datos);
        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(servicio.guardar(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!clubes.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clubes.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
