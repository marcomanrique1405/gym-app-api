\if :{?admin_email}
\else
\error 'Use: psql ... -v admin_email=... -v admin_name=... -v admin_password_hash=... -f scripts/create-first-admin.sql'
\endif

INSERT INTO usuarios (usuario_id, nombre, email, password, fecha_registro, rol, activo)
VALUES (gen_random_uuid(), :'admin_name', lower(trim(:'admin_email')),
        :'admin_password_hash', CURRENT_TIMESTAMP, 'ADMIN', true)
ON CONFLICT DO NOTHING;
