package com.gymtracker.gym_api.infrastructure.repository.auth;

import com.gymtracker.gym_api.domain.model.auth.Usuario;
import com.gymtracker.gym_api.domain.repository.auth.UsuarioRepository;
import com.gymtracker.gym_api.infrastructure.entity.auth.UsuarioEntity;
import com.gymtracker.gym_api.infrastructure.jpa.auth.UsuarioJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.auth.UsuarioMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioRepositoryImpl(UsuarioJpaRepository usuarioJpaRepository, UsuarioMapper usuarioMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }


    @Override
    public Usuario guardar(Usuario usuario) {

        UsuarioEntity entity = usuarioMapper.toEntity(usuario);

        UsuarioEntity guardado = usuarioJpaRepository.save(entity);

        return usuarioMapper.toDomain(guardado);

    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioJpaRepository.findByEmail(email)
                .map(usuarioMapper::toDomain);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioJpaRepository.findById(id)
                .map(usuarioMapper::toDomain);
    }
}
