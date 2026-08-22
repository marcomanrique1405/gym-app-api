CREATE TABLE usuarios (
    usuario_id UUID NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP(6) NOT NULL,
    rol VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (usuario_id),
    CONSTRAINT ck_usuarios_rol CHECK (rol IN ('USER', 'ADMIN'))
);

CREATE TABLE ejercicios (
    ejercicio_id UUID NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    grupo_muscular VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    activo BOOLEAN NOT NULL,
    CONSTRAINT pk_ejercicios PRIMARY KEY (ejercicio_id),
    CONSTRAINT ck_ejercicios_grupo_muscular CHECK (grupo_muscular IN (
        'PECHO', 'ESPALDA', 'PIERNA', 'HOMBRO', 'BICEPS', 'TRICEPS',
        'ABDOMEN', 'GLUTEO', 'PANTORRILLA', 'ANTEBRAZO', 'CARDIO',
        'CUERPO_COMPLETO'
    ))
);

CREATE TABLE rutinas (
    rutina_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo_progresion VARCHAR(20) NOT NULL,
    activa BOOLEAN NOT NULL,
    fecha_creacion TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_rutinas PRIMARY KEY (rutina_id),
    CONSTRAINT fk_rutinas_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios (usuario_id) ON DELETE RESTRICT,
    CONSTRAINT ck_rutinas_tipo_progresion
        CHECK (tipo_progresion IN ('MANUAL', 'AUTOMATICA'))
);

CREATE TABLE dias_rutina (
    dia_rutina_id UUID NOT NULL,
    rutina_id UUID NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    orden_dia INTEGER NOT NULL,
    CONSTRAINT pk_dias_rutina PRIMARY KEY (dia_rutina_id),
    CONSTRAINT uk_dia_rutina_orden UNIQUE (rutina_id, orden_dia),
    CONSTRAINT uk_dia_rutina_semana UNIQUE (rutina_id, dia_semana),
    CONSTRAINT fk_dias_rutina_rutina FOREIGN KEY (rutina_id)
        REFERENCES rutinas (rutina_id) ON DELETE RESTRICT,
    CONSTRAINT ck_dias_rutina_dia_semana CHECK (dia_semana IN (
        'LUNES', 'MARTES', 'MIERCOLES', 'JUEVES',
        'VIERNES', 'SABADO', 'DOMINGO'
    ))
);

CREATE TABLE ejercicios_rutina (
    ejercicio_rutina_id UUID NOT NULL,
    dia_rutina_id UUID NOT NULL,
    ejercicio_id UUID NOT NULL,
    orden INTEGER NOT NULL,
    peso_objetivo NUMERIC(6, 2) NOT NULL,
    incremento_peso NUMERIC(4, 2) NOT NULL,
    sobrecarga_activa BOOLEAN NOT NULL,
    activo BOOLEAN NOT NULL,
    CONSTRAINT pk_ejercicios_rutina PRIMARY KEY (ejercicio_rutina_id),
    CONSTRAINT fk_ejercicios_rutina_dia FOREIGN KEY (dia_rutina_id)
        REFERENCES dias_rutina (dia_rutina_id) ON DELETE RESTRICT,
    CONSTRAINT fk_ejercicios_rutina_ejercicio FOREIGN KEY (ejercicio_id)
        REFERENCES ejercicios (ejercicio_id) ON DELETE RESTRICT
);

CREATE TABLE series_rutina (
    serie_rutina_id UUID NOT NULL,
    ejercicio_rutina_id UUID NOT NULL,
    orden INTEGER NOT NULL,
    repeticiones_min INTEGER NOT NULL,
    repeticiones_max INTEGER NOT NULL,
    activo BOOLEAN NOT NULL,
    CONSTRAINT pk_series_rutina PRIMARY KEY (serie_rutina_id),
    CONSTRAINT fk_series_rutina_ejercicio_rutina
        FOREIGN KEY (ejercicio_rutina_id)
        REFERENCES ejercicios_rutina (ejercicio_rutina_id) ON DELETE RESTRICT,
    CONSTRAINT ck_serie_rutina_valores CHECK (
        orden > 0
        AND repeticiones_min > 0
        AND repeticiones_max >= repeticiones_min
    )
);

CREATE TABLE sesion_entrenamiento (
    sesion_entrenamiento_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    rutina_id UUID NOT NULL,
    fecha_inicio TIMESTAMP(6) NOT NULL,
    fecha_fin TIMESTAMP(6),
    finalizada BOOLEAN NOT NULL,
    CONSTRAINT pk_sesion_entrenamiento PRIMARY KEY (sesion_entrenamiento_id),
    CONSTRAINT fk_sesion_entrenamiento_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios (usuario_id) ON DELETE RESTRICT,
    CONSTRAINT fk_sesion_entrenamiento_rutina FOREIGN KEY (rutina_id)
        REFERENCES rutinas (rutina_id) ON DELETE RESTRICT,
    CONSTRAINT ck_sesion_fechas CHECK (
        fecha_fin IS NULL OR fecha_fin >= fecha_inicio
    )
);

CREATE TABLE series_entrenamiento (
    serie_entrenamiento_id UUID NOT NULL,
    sesion_entrenamiento_id UUID NOT NULL,
    serie_rutina_id UUID NOT NULL,
    repeticiones_realizadas INTEGER NOT NULL,
    peso_utilizado NUMERIC(10, 2) NOT NULL,
    CONSTRAINT pk_series_entrenamiento PRIMARY KEY (serie_entrenamiento_id),
    CONSTRAINT fk_series_entrenamiento_sesion
        FOREIGN KEY (sesion_entrenamiento_id)
        REFERENCES sesion_entrenamiento (sesion_entrenamiento_id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_series_entrenamiento_serie_rutina
        FOREIGN KEY (serie_rutina_id)
        REFERENCES series_rutina (serie_rutina_id) ON DELETE RESTRICT,
    CONSTRAINT ck_serie_entrenamiento_valores CHECK (
        repeticiones_realizadas > 0 AND peso_utilizado >= 0
    )
);

CREATE UNIQUE INDEX uk_usuarios_email_normalizado
    ON usuarios (lower(btrim(email)));
CREATE UNIQUE INDEX uk_ejercicio_nombre_activo
    ON ejercicios (lower(btrim(nombre))) WHERE activo = true;
CREATE UNIQUE INDEX uk_rutina_usuario_nombre_activa
    ON rutinas (usuario_id, lower(btrim(nombre))) WHERE activa = true;
CREATE UNIQUE INDEX uk_ejercicio_rutina_dia_orden_activo
    ON ejercicios_rutina (dia_rutina_id, orden) WHERE activo = true;
CREATE UNIQUE INDEX uk_ejercicio_rutina_dia_ejercicio_activo
    ON ejercicios_rutina (dia_rutina_id, ejercicio_id) WHERE activo = true;
CREATE UNIQUE INDEX uk_serie_rutina_orden_activa
    ON series_rutina (ejercicio_rutina_id, orden) WHERE activo = true;
CREATE UNIQUE INDEX uk_sesion_usuario_activa
    ON sesion_entrenamiento (usuario_id) WHERE finalizada = false;
CREATE UNIQUE INDEX uk_serie_entrenamiento_sesion_serie
    ON series_entrenamiento (sesion_entrenamiento_id, serie_rutina_id);

CREATE INDEX ix_rutinas_usuario_id ON rutinas (usuario_id);
CREATE INDEX ix_dias_rutina_rutina_id ON dias_rutina (rutina_id);
CREATE INDEX ix_ejercicios_rutina_dia_rutina_id
    ON ejercicios_rutina (dia_rutina_id);
CREATE INDEX ix_ejercicios_rutina_ejercicio_id
    ON ejercicios_rutina (ejercicio_id);
CREATE INDEX ix_series_rutina_ejercicio_rutina_id
    ON series_rutina (ejercicio_rutina_id);
CREATE INDEX ix_sesion_entrenamiento_usuario_id
    ON sesion_entrenamiento (usuario_id);
CREATE INDEX ix_sesion_entrenamiento_rutina_id
    ON sesion_entrenamiento (rutina_id);
CREATE INDEX ix_series_entrenamiento_sesion_id
    ON series_entrenamiento (sesion_entrenamiento_id);
CREATE INDEX ix_series_entrenamiento_serie_rutina_id
    ON series_entrenamiento (serie_rutina_id);
