package com.example.mundial.repositorio;

import com.example.mundial.modelo.Asociacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AsociacionRepositorio extends MongoRepository<Asociacion, Long> {
}
