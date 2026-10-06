# Mundial de Clubes – Spring Boot + MongoDB Atlas (no relacional)

Relaciones entre Club, Entrenador, Jugador, Asociación y Competición con
`@Document` y `@DocumentReference` (Spring Data MongoDB).

- Java 17, Spring Boot 3.5, Spring Data MongoDB, Thymeleaf + Bootstrap 5, API REST
- Puerto: **8089**

| JPA           | MongoDB                   | Ejemplo                |
|---------------|---------------------------|------------------------|
| `@OneToOne`   | `@DocumentReference`      | Club → Entrenador      |
| `@OneToMany`  | `@DocumentReference List` | Club → Jugadores       |
| `@ManyToOne`  | `@DocumentReference`      | Club → Asociación      |
| `@ManyToMany` | `@DocumentReference List` | Club ↔ Competiciones   |

## Ejecutar en local
1. Crear `.env` en la raíz con `MONGODB_URI=mongodb+srv://...`
2. Ejecutar `MundialApplication` y abrir http://localhost:8089

## API REST
`/api/clubes`, `/api/entrenadores`, `/api/jugadores`, `/api/asociaciones`, `/api/competiciones`

## Despliegue en Render (Docker)
Variables de entorno: `MONGODB_URI` y `PORT=8089`.
