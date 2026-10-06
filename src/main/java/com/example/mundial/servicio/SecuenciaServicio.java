package com.example.mundial.servicio;

import com.example.mundial.modelo.Secuencia;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/** Genera ids 1, 2, 3... con $inc atomico (MongoDB no tiene autoincremento). */
@Service
public class SecuenciaServicio {

    private final MongoOperations mongo;

    public SecuenciaServicio(MongoOperations mongo) {
        this.mongo = mongo;
    }

    public long generarId(String nombreSecuencia) {
        Secuencia s = mongo.findAndModify(
                new Query(Criteria.where("_id").is(nombreSecuencia)),
                new Update().inc("valor", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Secuencia.class);
        return s != null ? s.getValor() : 1;
    }
}
