# gym-app-api

API de gimnasio con Spring Boot, PostgreSQL, JWT y los roles exclusivos `ADMIN` y `USER`.

## Requisitos

- Java 17
- PostgreSQL
- Git

No es necesario instalar Maven: el repositorio incluye Maven Wrapper.

## Configuración local

No hay credenciales ni secretos predeterminados rastreados. `.env.example` documenta las variables requeridas, pero Spring Boot no carga automáticamente un archivo `.env`; defina las variables en su terminal o en la configuración de ejecución de su IDE.

### PowerShell (Windows)

```powershell
$env:SPRING_PROFILES_ACTIVE='dev'
$env:DB_URL='jdbc:postgresql://localhost:5432/gymapp'
$env:DB_USERNAME='gymapp_dev'
$env:DB_PASSWORD='contraseña-local'
$env:JWT_SECRET='<secreto-base64-de-al-menos-32-bytes>'
$env:CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5173'
.\mvnw.cmd spring-boot:run
```

Genere `JWT_SECRET` con una herramienta criptográficamente segura. Por ejemplo, con OpenSSL:

```powershell
openssl rand -base64 32
```

### Bash (Linux/macOS)

```bash
export SPRING_PROFILES_ACTIVE=dev
export DB_URL='jdbc:postgresql://localhost:5432/gymapp'
export DB_USERNAME='gymapp_dev'
export DB_PASSWORD='contraseña-local'
export JWT_SECRET="$(openssl rand -base64 32)"
export CORS_ALLOWED_ORIGINS='http://localhost:3000,http://localhost:5173'
./mvnw spring-boot:run
```

`JWT_SECRET` debe ser Base64 y representar al menos 32 bytes aleatorios. No reutilice el secreto anterior. También pueden configurarse `JWT_EXPIRATION_MS` y `SERVER_PORT`.

Producción requiere `SPRING_PROFILES_ACTIVE=prod` y todas las variables anteriores. El perfil no se selecciona automáticamente para evitar arrancar accidentalmente con una configuración incorrecta.

## Base de datos

La API usa una base PostgreSQL existente y **no ejecuta migraciones automáticamente**. Antes de desplegar esta versión, ejecute manualmente una vez `scripts/update-existing-schema.sql` para aplicar índices y restricciones sin crear tablas ni borrar datos. Hibernate usa `ddl-auto=validate`, por lo que una base nueva o pendiente de actualización puede impedir el arranque; este script no sustituye un mecanismo de creación inicial del esquema. Producción mantiene `show-sql=false`. Los índices parciales permiten reutilizar nombres y órdenes después del borrado lógico y garantizan una sola sesión activa por usuario.

Pruebe primero el script sobre una copia o respaldo reciente de PostgreSQL y conserve como evidencia la salida de `psql` y las consultas de verificación incluidas al final del archivo. La aplicación deliberadamente mantiene este procedimiento manual; no se incorpora Flyway/Liquibase ni se modifica la base automáticamente.

Los días se eliminan físicamente. Si tienen dependencias, PostgreSQL rechaza la eliminación y la API responde `409` sin exponer detalles SQL.

## Primer ADMIN

`POST /auth/register` siempre crea `USER` y el request no acepta roles. No existe un endpoint público para crear administradores.

Genere un hash BCrypt con costo 12 y ejecute una sola vez el script parametrizado. Use parámetros de conexión de `psql`, no la URL JDBC:

```bash
ADMIN_HASH="$(htpasswd -bnBC 12 '' 'cambie-esta-contraseña' | tr -d ':\n')"
PGPASSWORD="$DB_PASSWORD" psql \
  -h localhost -p 5432 -U "$DB_USERNAME" -d gymapp \
  -v admin_email='admin@example.com' \
  -v admin_name='Administrador' \
  -v admin_password_hash="$ADMIN_HASH" \
  -f scripts/create-first-admin.sql
```

Después, compruebe que se creó exactamente una fila `ADMIN` activa y cambie/borre de su historial cualquier variable que contenga la contraseña o el hash.

## Permisos

| Recurso | USER | ADMIN |
|---|---|---|
| Registro y login | Público | Público |
| Consultar ejercicios | Sí | Sí |
| Crear o actualizar ejercicios | No (`403`) | Sí |
| Rutinas, días, ejercicios asignados y series | Solo recursos propios | Solo recursos propios |
| Sesiones, progreso e historial | Solo recursos propios | Solo recursos propios |

Los endpoints normales nunca permiten que `ADMIN` evada la propiedad del recurso.

## Respuestas de error

Todos los errores usan `ApiError`: `timestamp`, `status`, `error`, `message` y `path`.

- `400`: JSON, UUID, enum o parámetros inválidos.
- `401`: token ausente, inválido, alterado, expirado o usuario inactivo/inexistente.
- `403`: rol insuficiente.
- `404`: recurso inexistente o ajeno.
- `405`: método HTTP incorrecto.
- `409`: duplicado, dependencia o conflicto de negocio.
- `422`: validación de DTO.
- `500`: error inesperado, sin detalles internos en la respuesta.

## Verificación

En Windows:

```powershell
.\mvnw.cmd clean verify
```

En Linux/macOS:

```bash
./mvnw clean verify
```

La línea base actual ejecuta 19 suites con 84 pruebas, sin fallos, errores ni pruebas omitidas.

Las pruebas normales usan H2 con el perfil `test`; no validan los índices parciales ni la ejecución del script manual de PostgreSQL. Para CI se recomienda añadir Testcontainers y ejecutar allí `scripts/update-existing-schema.sql` antes de afirmar que esas restricciones fueron verificadas.
