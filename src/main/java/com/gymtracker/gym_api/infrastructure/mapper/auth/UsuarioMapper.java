package com.gymtracker.gym_api.infrastructure.mapper.auth;

import com.gymtracker.gym_api.domain.model.auth.Usuario;
import com.gymtracker.gym_api.infrastructure.entity.auth.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();

        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail());
        entity.setPassword(usuario.getPassword());
        entity.setFechaRegistro(usuario.getFechaRegistro());

        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        return Usuario.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }

}
