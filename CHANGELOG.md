# Changelog

Este archivo registra los cambios relevantes publicados en Gym Tracker API.

## [1.0.0] - 2026-08-22

Primera versión estable del backend.

### Funcionalidades

- Registro e inicio de sesión con autenticación JWT.
- Roles `USER` y `ADMIN` con administración restringida del catálogo de ejercicios.
- Gestión de rutinas, días, ejercicios asignados y series planeadas.
- Inicio, consulta y finalización de sesiones de entrenamiento.
- Registro del rendimiento real por serie.
- Historial de sesiones paginado y ordenado de forma estable.
- Autorización por propietario para impedir el acceso a recursos de otros usuarios.
- Borrado lógico en los recursos que requieren conservar historial.

### Datos e infraestructura

- Esquema inicial de ocho tablas administrado con Flyway.
- Restricciones, relaciones e índices validados sobre PostgreSQL 16.12.
- Hibernate configurado con `ddl-auto=validate`.
- Entorno reproducible con Docker Compose, health checks y persistencia en volumen.
- Imagen Docker multi-stage ejecutada con un usuario sin privilegios.
- Configuración mediante variables de entorno sin secretos rastreados.

### Calidad y seguridad

- Seguridad stateless con Spring Security, JWT y BCrypt.
- Validación temprana de la clave y expiración JWT.
- Respuestas de error uniformes sin exposición de detalles internos.
- 89 pruebas automatizadas con JUnit 5, Mockito y MockMvc.
- Integración continua con GitHub Actions para pruebas Maven y construcción Docker.
- Documentación interactiva con OpenAPI y Swagger UI.

[1.0.0]: https://github.com/marcomanrique1405/gym-app-api/releases/tag/v1.0.0
