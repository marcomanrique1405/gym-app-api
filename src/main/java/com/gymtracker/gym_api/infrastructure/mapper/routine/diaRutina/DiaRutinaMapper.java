package com.gymtracker.gym_api.infrastructure.mapper.routine.diaRutina;

import com.gymtracker.gym_api.domain.model.routine.diaRutina.DiaRutina;
import com.gymtracker.gym_api.infrastructure.entity.routine.diaRutina.DiaRutinaEntity;
import org.springframework.stereotype.Component;

@Component
public class DiaRutinaMapper {

    public DiaRutinaEntity toEntity(DiaRutina diaRutina) {

        DiaRutinaEntity entity = new DiaRutinaEntity();

        entity.setId(diaRutina.getId());
        entity.setRutinaId(diaRutina.getRutinaId());
        entity.setDiaSemana(diaRutina.getDiaSemana());
        entity.setOrdenDia(diaRutina.getOrdenDia());

        return entity;

    }

    public DiaRutina toDomain(DiaRutinaEntity entity) {

        return DiaRutina.builder()
                .id(entity.getId())
                .rutinaId(entity.getRutinaId())
                .diaSemana(entity.getDiaSemana())
                .ordenDia(entity.getOrdenDia())
                .build();

    }

}
