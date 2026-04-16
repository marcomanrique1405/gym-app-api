package com.gymtracker.gym_api.infrastructure.mapper.routine;

import com.gymtracker.gym_api.domain.model.routine.Rutina;
import com.gymtracker.gym_api.infrastructure.entity.routine.RutinaEntity;
import org.springframework.stereotype.Component;

@Component
public class RutinaMapper {

     public RutinaEntity toEntity(Rutina rutina) {
         RutinaEntity entity = new RutinaEntity();

         entity.setId(rutina.getId());
         entity.setUsuarioId(rutina.getUsuarioId());
         entity.setNombre(rutina.getNombre());
         entity.setTipoProgresion(rutina.getTipoProgresion());
         entity.setActiva(rutina.getActiva());
         entity.setFechaCreacion(rutina.getFechaCreacion());

         return entity;

     }

     public Rutina toDomain(RutinaEntity entity) {
         return Rutina.builder()
                 .id(entity.getId())
                 .usuarioId(entity.getUsuarioId())
                 .nombre(entity.getNombre())
                 .tipoProgresion(entity.getTipoProgresion())
                 .activa(entity.getActiva())
                 .fechaCreacion(entity.getFechaCreacion())
                 .build();

     }

}
