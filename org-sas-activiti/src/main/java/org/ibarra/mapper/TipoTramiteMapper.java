package org.ibarra.mapper;


import org.ibarra.dto.TipoTramiteDto;
import org.ibarra.entity.TipoTramite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoTramiteMapper {
    TipoTramite toEntity(TipoTramiteDto dto);

    TipoTramiteDto toDto(TipoTramite entity);
}
