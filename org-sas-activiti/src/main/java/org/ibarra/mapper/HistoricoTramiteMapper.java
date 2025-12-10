package org.ibarra.mapper;

import org.ibarra.dto.HistoricoTramiteDto;
import org.ibarra.entity.HistoricoTramite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TipoTramiteMapper.class})
public interface HistoricoTramiteMapper {

    //@Mapping(target = "solicitante")
    HistoricoTramite toEntity(HistoricoTramiteDto dto);

//    @Mapping(target = "solicitante", ignore = true)
    HistoricoTramiteDto toDto(HistoricoTramite entity);

}
