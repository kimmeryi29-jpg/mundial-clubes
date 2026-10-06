package com.example.mundial.servicio;

import com.example.mundial.modelo.EntidadConId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * Se ejecuta justo antes de guardar cualquier documento.
 * Si la entidad es nueva (id == null) le asigna el siguiente numero.
 * Cada clase tiene su propio contador: "club", "jugador", "entrenador"...
 */
@Component
public class GeneradorIdListener extends AbstractMongoEventListener<Object> {

    private final SecuenciaServicio secuencia;

    public GeneradorIdListener(SecuenciaServicio secuencia) {
        this.secuencia = secuencia;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        if (event.getSource() instanceof EntidadConId entidad && entidad.getId() == null) {
            String nombre = entidad.getClass().getSimpleName().toLowerCase();
            entidad.setId(secuencia.generarId(nombre));
        }
    }
}
