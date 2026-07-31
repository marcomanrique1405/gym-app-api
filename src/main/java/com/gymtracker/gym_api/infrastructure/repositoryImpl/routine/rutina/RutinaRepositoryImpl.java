package com.gymtracker.gym_api.infrastructure.repositoryImpl.routine.rutina;

import com.gymtracker.gym_api.domain.model.routine.rutina.Rutina;
import com.gymtracker.gym_api.domain.repository.routine.rutina.RutinaRepository;
import com.gymtracker.gym_api.infrastructure.entity.routine.rutina.RutinaEntity;
import com.gymtracker.gym_api.infrastructure.jpa.routine.rutina.RutinaJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.routine.rutina.RutinaMapper;
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
        return rutinaJpaRepository.existsByUsuarioIdAndNombreIgnoreCaseAndActivaTrue(usuarioId, nombre);
    }

    @Override
    public List<Rutina> obtenerPorUsuarioIdYActivaTrue(UUID usuarioId) {
        return rutinaJpaRepository
                .findByUsuarioIdAndActivaTrue(usuarioId)
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

    @Override
    public Optional<Rutina> obtenerPorIdYUsuarioId(UUID id, UUID usuarioId) {
        return rutinaJpaRepository
                .findByIdAndUsuarioIdAndActivaTrue(id, usuarioId)
                .map(rutinaMapper::toDomain);
    }

}
