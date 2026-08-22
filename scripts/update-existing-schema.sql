-- LEGACY: ajustes manuales para bases PostgreSQL creadas antes de Flyway V1.
-- No ejecute este script sobre una base que ya administre Flyway.
-- No crea tablas, no elimina registros y puede ejecutarse más de una vez.
-- Haga un respaldo antes de aplicarlo.

BEGIN;

-- Estas columnas son obligatorias según los DTO y entidades de la API.
ALTER TABLE sesion_entrenamiento ALTER COLUMN usuario_id SET NOT NULL;
ALTER TABLE sesion_entrenamiento ALTER COLUMN rutina_id SET NOT NULL;
ALTER TABLE sesion_entrenamiento ALTER COLUMN fecha_inicio SET NOT NULL;
ALTER TABLE sesion_entrenamiento ALTER COLUMN finalizada SET NOT NULL;
ALTER TABLE series_entrenamiento ALTER COLUMN peso_utilizado SET NOT NULL;

-- Retira restricciones globales incompatibles con el borrado lógico.
ALTER TABLE ejercicios DROP CONSTRAINT IF EXISTS uk_ejercicio_nombre;
ALTER TABLE rutinas DROP CONSTRAINT IF EXISTS uk_rutina_usuario_nombre;
ALTER TABLE ejercicios_rutina DROP CONSTRAINT IF EXISTS uk_ejercicio_rutina_dia_orden;
ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_email_key;

-- Unicidad normalizada y compatible con registros inactivos.
CREATE UNIQUE INDEX IF NOT EXISTS uk_usuarios_email_normalizado
    ON usuarios (lower(btrim(email)));
CREATE UNIQUE INDEX IF NOT EXISTS uk_ejercicio_nombre_activo
    ON ejercicios (lower(btrim(nombre))) WHERE activo = true;
CREATE UNIQUE INDEX IF NOT EXISTS uk_rutina_usuario_nombre_activa
    ON rutinas (usuario_id, lower(btrim(nombre))) WHERE activa = true;
CREATE UNIQUE INDEX IF NOT EXISTS uk_ejercicio_rutina_dia_orden_activo
    ON ejercicios_rutina (dia_rutina_id, orden) WHERE activo = true;
CREATE UNIQUE INDEX IF NOT EXISTS uk_serie_rutina_orden_activa
    ON series_rutina (ejercicio_rutina_id, orden) WHERE activo = true;

-- Impide carreras: una sesión activa por usuario y una captura por serie/sesión.
CREATE UNIQUE INDEX IF NOT EXISTS uk_sesion_usuario_activa
    ON sesion_entrenamiento (usuario_id) WHERE finalizada = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_serie_entrenamiento_sesion_serie
    ON series_entrenamiento (sesion_entrenamiento_id, serie_rutina_id);

-- Checks idempotentes para roles y valores de negocio.
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'ck_usuarios_rol') THEN
        ALTER TABLE usuarios ADD CONSTRAINT ck_usuarios_rol CHECK (rol IN ('ADMIN', 'USER'));
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'ck_serie_rutina_valores') THEN
        ALTER TABLE series_rutina ADD CONSTRAINT ck_serie_rutina_valores
            CHECK (orden > 0 AND repeticiones_min > 0 AND repeticiones_max >= repeticiones_min);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'ck_serie_entrenamiento_valores') THEN
        ALTER TABLE series_entrenamiento ADD CONSTRAINT ck_serie_entrenamiento_valores
            CHECK (repeticiones_realizadas > 0 AND peso_utilizado >= 0);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'ck_sesion_fechas') THEN
        ALTER TABLE sesion_entrenamiento ADD CONSTRAINT ck_sesion_fechas
            CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio);
    END IF;
END $$;

COMMIT;

-- Diagnóstico pendiente: la base actual contiene asignaciones activas repetidas.
-- Este SELECT no modifica datos; úselo para decidir manualmente cuál conservar.
SELECT dia_rutina_id, ejercicio_id, count(*) AS repeticiones
FROM ejercicios_rutina
WHERE activo = true
GROUP BY dia_rutina_id, ejercicio_id
HAVING count(*) > 1;

-- Después de sanear el resultado anterior, puede reforzarlo también en BD:
-- CREATE UNIQUE INDEX uk_ejercicio_rutina_dia_ejercicio_activo
--     ON ejercicios_rutina (dia_rutina_id, ejercicio_id) WHERE activo = true;
