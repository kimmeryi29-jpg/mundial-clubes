package com.example.mundial.repositorio;

import com.example.mundial.modelo.Jugador;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JugadorRepositorio extends MongoRepository<Jugador, Long> {
}
