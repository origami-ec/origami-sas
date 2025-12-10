package org.sas.administrativo.mapper.talentohumano;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.sas.administrativo.dto.UnidadAdministrativaDto;
import org.sas.administrativo.entity.talentohumano.UnidadAdministrativa;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring" )
@Component
public interface UnidadAdministrativaMapper {

    UnidadAdministrativa toEntity(UnidadAdministrativaDto dto);

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "codigo", source = "entity.codigo"),
            @Mapping(target = "estado", source = "entity.estado"),
            @Mapping(target = "ubicacion", source = "entity.ubicacion"),
            @Mapping(target = "nombre", source = "entity.nombre")
    })
    UnidadAdministrativaDto toDto(UnidadAdministrativa entity);

    List<UnidadAdministrativa> toEntity(List<UnidadAdministrativaDto> dto);

    List<UnidadAdministrativaDto> toDto(List<UnidadAdministrativa> entity);
}
