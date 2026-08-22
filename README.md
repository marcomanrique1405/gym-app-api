# Gym Tracker API

[![CI](https://github.com/marcomanrique1405/gym-app-api/actions/workflows/ci.yml/badge.svg)](https://github.com/marcomanrique1405/gym-app-api/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)

API REST para planificar rutinas y registrar el progreso de entrenamiento de cada usuario. El proyecto cubre el flujo completo: autenticación, creación de rutinas, organización por días, configuración de ejercicios y series, inicio y finalización de sesiones, y consulta paginada del historial.

Más que un CRUD, este backend aplica reglas de propiedad, autorización por rol, migraciones versionadas, restricciones de integridad en PostgreSQL, manejo uniforme de errores y una separación clara entre dominio, casos de uso e infraestructura.

> Estado: candidato a `v1.0.0`. La implementación fue validada con Java 17, PostgreSQL 16.12, Flyway 12.4, Docker Compose y **89 pruebas automatizadas**.

## Contenido

- [Qué permite hacer](#qué-permite-hacer)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Modelo de datos](#modelo-de-datos)
- [Endpoints principales](#endpoints-principales)
- [Ejecución con Docker](#ejecución-con-docker)
- [Ejecución local](#ejecución-local)
- [Seguridad y permisos](#seguridad-y-permisos)
- [Migraciones con Flyway](#migraciones-con-flyway)
- [Pruebas y CI](#pruebas-y-ci)
- [Autor](#autor)

## Qué permite hacer

- Registrar usuarios e iniciar sesión mediante JWT.
- Administrar ejercicios globales con permisos exclusivos para `ADMIN`.
- Crear, consultar, actualizar y eliminar rutinas propias.
- Organizar cada rutina por días de la semana.
- Asignar ejercicios a los días y definir series, repeticiones, descanso y peso objetivo.
- Iniciar una sesión de entrenamiento a partir de una rutina.
- Registrar el rendimiento real de cada serie durante la sesión.
- Finalizar sesiones y consultar un historial paginado.
- Impedir que un usuario consulte o modifique recursos pertenecientes a otra cuenta.
- Mantener datos históricos mediante borrado lógico donde corresponde.

## Arquitectura

El código está organizado por responsabilidades y conserva el dominio separado de los detalles de persistencia:

```text
HTTP request
    │
    ▼
Controller ──► Application / Use case ──► Domain repository (port)
                                              │
                                              ▼
                                    Infrastructure adapter
                                              │
                                              ▼
                                      Spring Data JPA
                                              │
                                              ▼
                                         PostgreSQL
```

```text
src/main/java/com/gymtracker/gym_api
├── controller/       # Endpoints REST y validación de entrada
├── application/
│   ├── dto/          # Contratos de entrada y salida
│   └── usecase/      # Reglas y operaciones de la aplicación
├── domain/
│   ├── model/        # Modelos de dominio sin dependencias de frameworks
│   ├── repository/   # Puertos de persistencia
│   └── enums/        # Valores controlados del dominio
├── infrastructure/
│   ├── config/       # Seguridad y configuración técnica
│   ├── entity/       # Entidades JPA
│   ├── jpa/          # Repositorios Spring Data
│   ├── mapper/       # Conversión dominio ↔ persistencia
│   ├── repositoryImpl/
│   └── security/     # JWT, filtros y respuestas 401/403
└── shared/           # Excepciones, errores y validaciones reutilizables
```

Los controladores no acceden directamente a JPA. Los casos de uso trabajan con interfaces del dominio y la infraestructura proporciona sus implementaciones.

## Tecnologías

| Área | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 4.1.0 |
| API REST | Spring Web MVC |
| Seguridad | Spring Security, JWT (JJWT 0.13.0), BCrypt |
| Persistencia | Spring Data JPA, Hibernate |
| Base de datos | PostgreSQL 16.12 |
| Migraciones | Flyway 12.4 |
| Validación | Jakarta Bean Validation y validadores propios |
| Documentación | OpenAPI 3 y Swagger UI |
| Pruebas | JUnit 5, Mockito, MockMvc y H2 |
| Entorno | Docker, Docker Compose y Maven Wrapper |
| Integración continua | GitHub Actions |

## Modelo de datos

Flyway crea ocho tablas de negocio:

```text
usuarios
└── rutinas
    ├── dias_rutina
    │   └── ejercicios_rutina
    │       └── series_rutina
    └── sesion_entrenamiento
        └── series_entrenamiento

ejercicios ──────────► ejercicios_rutina
```

El esquema incluye claves foráneas, restricciones `CHECK`, índices para relaciones e índices únicos parciales. Entre las reglas protegidas por PostgreSQL se encuentran:

- Un email normalizado único por usuario.
- Una sola sesión activa por usuario.
- Una sola captura de cada serie dentro de una sesión.
- Rangos válidos para repeticiones y peso utilizado.
- Fechas de inicio y finalización consistentes.
- Reutilización controlada de nombres y posiciones después del borrado lógico.

## Endpoints principales

La especificación interactiva completa está disponible en Swagger UI después de iniciar la aplicación:

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/auth/register` | Público | Registrar un usuario `USER` |
| `POST` | `/auth/login` | Público | Autenticar y obtener un JWT |
| `GET` | `/ejercicio/{grupoMuscular}` | `USER`, `ADMIN` | Consultar ejercicios por grupo muscular |
| `POST` | `/ejercicio` | `ADMIN` | Crear un ejercicio global |
| `PATCH` | `/ejercicio/{id}` | `ADMIN` | Actualizar un ejercicio global |
| `POST` | `/rutinas` | Autenticado | Crear una rutina propia |
| `GET` | `/rutinas` | Autenticado | Consultar rutinas propias |
| `GET` | `/rutinas/{id}` | Propietario | Consultar una rutina |
| `PATCH` | `/rutinas/{id}` | Propietario | Actualizar una rutina |
| `DELETE` | `/rutinas/{id}` | Propietario | Eliminar lógicamente una rutina |
| `POST/GET` | `/rutinas/{rutinaId}/dias` | Propietario | Crear o consultar días |
| `PATCH/DELETE` | `/rutinas/{rutinaId}/dias/{diaRutinaId}` | Propietario | Modificar o eliminar un día |
| `POST` | `/rutinas/{rutinaId}/ejercicios-rutina` | Propietario | Asignar un ejercicio a un día |
| `PATCH` | `/rutinas/{rutinaId}/ejercicios-rutina/{ejercicioRutinaId}` | Propietario | Modificar una asignación |
| `DELETE` | `/rutinas/{rutinaId}/ejercicios-rutina/{ejercicioRutinaId}/dias/{diaRutinaId}` | Propietario | Retirar una asignación de un día |
| `POST/GET` | `/rutinas/{rutinaId}/dias/{diaId}/ejercicios-rutina/{ejercicioId}/series` | Propietario | Crear o consultar series planeadas |
| `POST` | `/sesiones-entrenamiento/rutinas/{rutinaId}` | Propietario | Iniciar una sesión |
| `GET` | `/sesiones-entrenamiento/activa` | Autenticado | Consultar la sesión activa |
| `PATCH` | `/sesiones-entrenamiento/{sesionId}/finalizar` | Propietario | Finalizar una sesión |
| `GET` | `/sesiones-entrenamiento?page=0&size=20` | Autenticado | Consultar historial paginado |
| `POST/GET` | `/sesiones-entrenamiento/{sesionId}/series` | Propietario | Registrar o consultar resultados |
| `PATCH` | `/sesiones-entrenamiento/{sesionId}/series/{serieId}` | Propietario | Actualizar el resultado de una serie |
| `GET` | `/actuator/health` | Público | Comprobar salud de la API |

Para usar una ruta protegida desde Swagger, inicie sesión, copie el valor de `token` y selecciónelo en **Authorize** como Bearer token.

## Ejecución con Docker

Esta es la forma recomendada porque solo requiere Git, Docker y Docker Compose v2. PostgreSQL no se publica al host; la API se conecta a la base mediante la red interna de Compose.

### 1. Clonar y preparar variables

```bash
git clone https://github.com/marcomanrique1405/gym-app-api.git
cd gym-app-api
cp .env.example .env
openssl rand -base64 32
```

Abra `.env` y reemplace, como mínimo:

```dotenv
POSTGRES_PASSWORD=una_contraseña_local_segura
JWT_SECRET=el_valor_base64_generado
```

El archivo `.env` está ignorado por Git. `JWT_SECRET` debe ser Base64 y representar al menos 32 bytes aleatorios.

### 2. Construir e iniciar

```bash
docker compose config --quiet
docker compose up --build --wait
```

Servicios disponibles:

| Servicio | URL |
|---|---|
| API | `http://localhost:8081` |
| Swagger UI | `http://localhost:8081/swagger-ui/index.html` |
| Health check | `http://localhost:8081/actuator/health` |

La imagen usa compilación multi-stage y ejecuta la aplicación con un usuario sin privilegios. Los datos permanecen en el volumen `<proyecto>_postgres-data` después de `restart` o `down`.

### 3. Detener el entorno

```bash
docker compose down
```

Para una prueba completamente aislada y desechable:

```bash
docker compose -p gym-portfolio-test up --build --wait
docker compose -p gym-portfolio-test ps
docker compose -p gym-portfolio-test down -v
```

`down -v` elimina los datos del proyecto indicado. No lo use sobre un entorno cuyo volumen necesite conservar.

## Variables de entorno

| Variable | Requerida | Uso |
|---|---:|---|
| `SPRING_PROFILES_ACTIVE` | Sí | Perfil `dev` o `prod` |
| `POSTGRES_DB` | Con Compose | Base creada por PostgreSQL |
| `POSTGRES_USER` | Con Compose | Usuario de PostgreSQL |
| `POSTGRES_PASSWORD` | Con Compose | Contraseña de PostgreSQL |
| `DB_URL` | Fuera de Compose | URL JDBC de PostgreSQL |
| `DB_USERNAME` | Fuera de Compose | Usuario JDBC |
| `DB_PASSWORD` | Fuera de Compose | Contraseña JDBC |
| `JWT_SECRET` | Sí | Clave Base64 de al menos 32 bytes |
| `JWT_EXPIRATION_MS` | No | Vigencia del token; predeterminado: `3600000` |
| `SERVER_PORT` | No | Puerto HTTP; predeterminado: `8081` |
| `CORS_ALLOWED_ORIGINS` | No | Orígenes permitidos separados por coma |

La aplicación valida la configuración JWT al arrancar y falla inmediatamente si el secreto no es Base64 válido, tiene menos de 32 bytes o la expiración no es positiva.

## Ejecución local

Requisitos: Java 17 y PostgreSQL. Maven no necesita instalarse porque el repositorio incluye Maven Wrapper.

Primero cree una base vacía; Flyway construirá su esquema al iniciar la API:

```sql
CREATE DATABASE gymapp;
```

### Linux/macOS

```bash
export SPRING_PROFILES_ACTIVE=dev
export DB_URL='jdbc:postgresql://localhost:5432/gymapp'
export DB_USERNAME='gymapp_dev'
export DB_PASSWORD='contraseña-local'
export JWT_SECRET="$(openssl rand -base64 32)"
export CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5173'
./mvnw spring-boot:run
```

### PowerShell

```powershell
$env:SPRING_PROFILES_ACTIVE='dev'
$env:DB_URL='jdbc:postgresql://localhost:5432/gymapp'
$env:DB_USERNAME='gymapp_dev'
$env:DB_PASSWORD='contraseña-local'
$env:JWT_SECRET='<secreto-base64-de-al-menos-32-bytes>'
$env:CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5173'
.\mvnw.cmd spring-boot:run
```

Spring Boot ejecutado directamente no carga `.env` automáticamente; exporte las variables en la terminal o configúrelas en el IDE.

## Seguridad y permisos

La autenticación es stateless. Cada solicitud protegida debe incluir:

```http
Authorization: Bearer <JWT>
```

| Recurso | `USER` | `ADMIN` |
|---|---|---|
| Registro y login | Público | Público |
| Consultar ejercicios | Sí | Sí |
| Crear o actualizar ejercicios | No (`403`) | Sí |
| Rutinas, días, asignaciones y series | Solo recursos propios | Solo recursos propios |
| Sesiones, resultados e historial | Solo recursos propios | Solo recursos propios |

Ser `ADMIN` no permite evadir la propiedad de rutinas o sesiones. El rol únicamente agrega la administración del catálogo global de ejercicios.

`POST /auth/register` siempre crea un `USER`; el cliente no puede elegir el rol. Para preparar el primer administrador existe el script parametrizado [`scripts/create-first-admin.sql`](scripts/create-first-admin.sql), que requiere un hash BCrypt y acceso directo autorizado a PostgreSQL. No agregue un endpoint público para elevar roles.

Las respuestas de error mantienen el mismo contrato:

```json
{
  "timestamp": "2026-08-21T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso no encontrado",
  "path": "/rutinas/00000000-0000-0000-0000-000000000000"
}
```

Estados contemplados: `400` para formato o parámetros inválidos, `401` para autenticación inválida, `403` para permisos insuficientes, `404` para recursos inexistentes o ajenos, `409` para conflictos de negocio, `422` para validación y `500` sin exposición de detalles internos.

## Migraciones con Flyway

Al conectarse a una base PostgreSQL vacía:

1. Flyway crea `flyway_schema_history`.
2. Ejecuta [`V1__create_initial_schema.sql`](src/main/resources/db/migration/V1__create_initial_schema.sql).
3. Registra la versión y su checksum.
4. Hibernate ejecuta `ddl-auto=validate` para comprobar que las entidades coinciden con el esquema, sin modificarlo.

Una migración aplicada y compartida no debe editarse. Los cambios futuros deben agregarse como `V2__descripcion.sql`, `V3__descripcion.sql`, etc.

Las bases creadas antes de incorporar Flyway no deben recibir V1 encima de tablas existentes. Si los datos son prescindibles, recree la base para que Flyway la construya. [`scripts/update-existing-schema.sql`](scripts/update-existing-schema.sql) se mantiene únicamente como apoyo legado y no sustituye una migración ni registra un baseline.

## Pruebas y CI

Ejecute la verificación completa:

```bash
./mvnw clean verify
```

En Windows:

```powershell
.\mvnw.cmd clean verify
```

Línea base validada:

```text
Tests run: 89
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Las 89 pruebas cubren casos de uso, autenticación JWT, autorización, propiedad de recursos, validaciones numéricas, controladores y paginación. El perfil `test` utiliza H2 para pruebas rápidas; la migración V1 y las restricciones específicas de PostgreSQL se validaron adicionalmente sobre PostgreSQL 16.12 real.

El workflow de GitHub Actions se ejecuta en cada `push` y Pull Request:

1. Configura Java 17.
2. Ejecuta `./mvnw --batch-mode clean verify`.
3. Construye la imagen Docker.

La validación funcional en Docker también comprobó el health check, Swagger, registro, login, uso del JWT, creación de rutina, inicio de sesión de entrenamiento, historial paginado y rechazo de parámetros de paginación inválidos.

## Decisiones técnicas destacadas

- **JWT stateless:** evita sesiones almacenadas en el servidor y autentica cada solicitud.
- **Autorización por propietario:** conocer un UUID ajeno no permite acceder al recurso.
- **Flyway + `ddl-auto=validate`:** el esquema se versiona explícitamente y Hibernate solo lo verifica.
- **Restricciones en PostgreSQL:** las invariantes importantes no dependen únicamente del código Java.
- **Borrado lógico:** conserva historial y permite aplicar unicidad únicamente a registros activos.
- **Paginación estable:** el historial ordena por fecha e ID y limita `size` a un máximo de 100.
- **Docker reproducible:** API y PostgreSQL arrancan con health checks y dependencias controladas.
- **Errores uniformes:** clientes y frontend reciben una estructura predecible sin detalles sensibles.

## Autor

**Marco Antonio Manrique Castro**<br>
Backend Developer — Java · Spring Boot · PostgreSQL · REST APIs · Docker

- [GitHub](https://github.com/marcomanrique1405)
- [LinkedIn](https://www.linkedin.com/in/marco-antonio-manrique-castro-994364399/)

---

Este repositorio fue desarrollado como proyecto de portafolio para demostrar diseño de APIs REST, reglas de negocio, seguridad, persistencia relacional, pruebas automatizadas y entrega reproducible de un backend Java.
