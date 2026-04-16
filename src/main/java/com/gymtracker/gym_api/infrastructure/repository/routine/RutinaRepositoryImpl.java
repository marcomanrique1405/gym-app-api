package com.gymtracker.gym_api.infrastructure.repository.routine;

import com.gymtracker.gym_api.domain.model.routine.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.entity.routine.RutinaEntity;
import com.gymtracker.gym_api.infrastructure.jpa.routine.RutinaJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.routine.RutinaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RutinaRepositoryImpl implements RutinaRepository {

    private final RutinaJpaRepository rutinaJpaRepository;
    private final RutinaMapper rutinaMapper;

    public RutinaRepositoryImpl(RutinaJpaRepository rutinaJpaRepository, RutinaMapper rutinaMapper) {
        this.rutinaJpaRepository = rutinaJpaRepository;
        this.rutinaMapper = rutinaMapper;
    }


    @Override
    public Rutina save(Rutina rutina) {

        RutinaEntity rutinaEntity = rutinaMapper.toEntity(rutina);

        RutinaEntity guardado = rutinaJpaRepository.save(rutinaEntity);

        return rutinaMapper.toDomain(guardado);

    }

    @Override
    public boolean existePorUsuarioIdYNombre(UUID usuarioId, String nombre) {
        return rutinaJpaRepository.existsByUsuarioIdAndNombre(usuarioId, nombre);
    }

    @Override
    public List<Rutina> obtenerPorUsuarioId(UUID usuarioId) {
        return rutinaJpaRepository
                .findByUsuarioId(usuarioId)
                .stream()
                .map(rutinaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Rutina> obtenerPorId(UUID id) {
        return rutinaJpaRepository
                .findById(id)
                .map(rutinaMapper::toDomain);

    }

    @Override
    public boolean existePorId(UUID id) {
        return rutinaJpaRepository.existsById(id);
    }
}
