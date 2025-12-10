package org.sas.seguridad.mapper;

import org.sas.seguridad.dto.MesaAyudaDto;
import org.sas.seguridad.entity.MesaAyuda;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(
        componentModel = "spring"
)
public interface MesaAyudaMapper {
    MesaAyudaDto toDto(MesaAyuda entity);

    MesaAyuda toEntity(MesaAyudaDto dto);

    List<MesaAyudaDto> toDto(List<MesaAyuda> entity);

    List<MesaAyuda> toEntity(List<MesaAyudaDto> dto);
}