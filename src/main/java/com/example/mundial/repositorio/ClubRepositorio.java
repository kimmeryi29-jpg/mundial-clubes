package com.example.mundial.repositorio;

import com.example.mundial.modelo.Club;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Como @DocumentReference guarda solo el id, se puede preguntar directamente
 * si algun club usa un entrenador, jugador, asociacion o competicion.
 * Se usa para no dejar borrar algo que esta en uso (como la "foreign key"
 * con "no action" de las diapositivas 28 y 29).
 */
public interface ClubRepositorio extends MongoRepository<Club, Long> {

    @Query(value = "{ 'entrenador': ?0 }", exists = true)
    boolean usaEntrenador(Long entrenadorId);

    @Query(value = "{ 'jugadores': ?0 }", exists = true)
    boolean usaJugador(Long jugadorId);

    @Query(value = "{ 'asociacion': ?0 }", exists = true)
    boolean usaAsociacion(Long asociacionId);

    @Query(value = "{ 'competiciones': ?0 }", exists = true)
    boolean usaCompeticion(Long competicionId);
}
