package com.gymtracker.gym_api.domain.repository.auth;

import com.gymtracker.gym_api.domain.model.auth.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);

    Optional<Usuario> buscarPorId(UUID id);

}
