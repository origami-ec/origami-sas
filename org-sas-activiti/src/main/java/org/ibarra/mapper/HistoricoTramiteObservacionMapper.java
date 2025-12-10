package org.ibarra.mapper;

import org.ibarra.dto.HistoricoTramiteObservacionDto;
import org.ibarra.entity.HistoricoTramiteObservacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {HistoricoTramiteMapper.class})
public interface HistoricoTramiteObservacionMapper {

    @Mapping(target = "tramite.id", source = "dto.tramite")
    HistoricoTramiteObservacion toEntity(HistoricoTramiteObservacionDto dto);

    @Mapping(target = "tramite", source = "entity.tramite.id")
    HistoricoTramiteObservacionDto toDto(HistoricoTramiteObservacion entity);

    List<HistoricoTramiteObservacion> toEntity(List<HistoricoTramiteObservacionDto>  dto);

    List<HistoricoTramiteObservacionDto> toDto(List<HistoricoTramiteObservacion> entity);


}
