package com.example.mundial.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Club: es el PROPIETARIO de todas las relaciones (diapositivas 3, 9, 14, 18 y 22).
 *
 * En JPA se usan @OneToOne, @OneToMany, @ManyToOne y @ManyToMany.
 * En MongoDB se usa @DocumentReference: en el documento del club solo se guarda
 * el id del objeto relacionado, y Spring lo carga al leer el club.
 *
 * Asi queda guardado un club en Atlas:
 * { _id: 1, nombre: "Millonarios", entrenador: 1, jugadores: [1, 2],
 *   asociacion: 1, competiciones: [1, 2] }
 *
 * Carga eager/lazy (diapositivas 24-27): por defecto la referencia es EAGER
 * (se carga al leer el club). Con @DocumentReference(lazy = true) se cargaria
 * solo cuando se accede al atributo.
 */
@Document(collection = "clubes")
public class Club implements EntidadConId {

    @Id
    private Long id;

    private String nombre;

    // @OneToOne -> un club tiene un entrenador y un entrenador solo un club
    @DocumentReference
    private Entrenador entrenador;

    // @OneToMany -> un club tiene muchos jugadores; cada jugador es de un solo club
    @DocumentReference
    private List<Jugador> jugadores = new ArrayList<>();

    // @ManyToOne -> muchos clubes pertenecen a una asociacion
    @DocumentReference
    private Asociacion asociacion;

    // @ManyToMany -> un club juega muchas competiciones y una competicion tiene muchos clubes
    @DocumentReference
    private List<Competicion> competiciones = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }
    public Asociacion getAsociacion() { return asociacion; }
    public void setAsociacion(Asociacion asociacion) { this.asociacion = asociacion; }
    public List<Competicion> getCompeticiones() { return competiciones; }
    public void setCompeticiones(List<Competicion> competiciones) { this.competiciones = competiciones; }
}
