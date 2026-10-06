package com.example.mundial.repositorio;

import com.example.mundial.modelo.Competicion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompeticionRepositorio extends MongoRepository<Competicion, Long> {
}
