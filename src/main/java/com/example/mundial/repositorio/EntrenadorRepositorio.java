package com.example.mundial.repositorio;

import com.example.mundial.modelo.Entrenador;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntrenadorRepositorio extends MongoRepository<Entrenador, Long> {
}
