package com.gymtracker.gym_api.infrastructure.repositoryImpl.routine.diaRutina;

import com.gymtracker.gym_api.domain.enums.DiaSemana;
import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.domain.repository.routine.diaRutina.DiaRutinaRepository;
import com.gymtracker.gym_api.infrastructure.entity.routine.diaRutina.DiaRutinaEntity;
import com.gymtracker.gym_api.infrastructure.jpa.routine.diaRutina.DiaRutinaJpaRepository;
import com.gymtracker.gym_api.infrastructure.mapper.routine.diaRutina.DiaRutinaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DiaRutinaRepositoryImpl implements DiaRutinaRepository {

    private final DiaRutinaJpaRepository diaRutinaJpaRepository;
    private final DiaRutinaMapper diaRutinaMapper;

    public DiaRutinaRepositoryImpl(DiaRutinaJpaRepository diaRutinaJpaRepository, DiaRutinaMapper diaRutinaMapper) {
        this.diaRutinaJpaRepository = diaRutinaJpaRepository;
        this.diaRutinaMapper = diaRutinaMapper;
    }

    @Override
    public DiaRutina save(DiaRutina diaRutina) {

        DiaRutinaEntity entity = diaRutinaMapper.toEntity(diaRutina);

        DiaRutinaEntity savedEntity = diaRutinaJpaRepository.save(entity);

        return diaRutinaMapper.toDomain(savedEntity);

    }

    @Override
    public List<DiaRutina> obtenerPorRutinaId(UUID rutinaId) {
        return diaRutinaJpaRepository
                .findByRutinaIdOrderByOrdenDiaAsc(rutinaId)
                .stream()
                .map(diaRutinaMapper::toDomain)
                .toList();

    }

    @Override
    public Optional<DiaRutina> obtenerPorIdYRutinaId(UUID id, UUID rutinaId) {
        return diaRutinaJpaRepository
                .findByIdAndRutinaId(id, rutinaId)
                .map(diaRutinaMapper::toDomain);
    }

    @Override
    public void delete(DiaRutina diaRutina) {

        DiaRutinaEntity entity = diaRutinaMapper.toEntity(diaRutina);

        diaRutinaJpaRepository.delete(entity);
    }

    @Override
    public boolean existePorRutinaIdYDiaSemana(UUID rutinaId, DiaSemana diaSemana) {
        return diaRutinaJpaRepository
                .existsByRutinaIdAndDiaSemana(rutinaId, diaSemana);
    }

    @Override
    public int contarDiasPorRutinaId(UUID rutinaId) {
        return diaRutinaJpaRepository.countByRutinaId(rutinaId);
    }

    @Override
    public boolean existePorRutinaIdYOrdenDia(UUID rutinaId, Integer ordenDia) {
        return diaRutinaJpaRepository
                .existsByRutinaIdAndOrdenDia(rutinaId, ordenDia);
    }

}
