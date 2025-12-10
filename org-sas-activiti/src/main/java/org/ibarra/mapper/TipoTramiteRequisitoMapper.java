package org.ibarra.mapper;

import org.ibarra.dto.TipoTramiteRequisitoDto;
import org.ibarra.entity.TipoTramiteRequisito;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoTramiteRequisitoMapper {

    TipoTramiteRequisito toEntity(TipoTramiteRequisitoDto dto);
    TipoTramiteRequisitoDto toDto(TipoTramiteRequisito entity);
}
