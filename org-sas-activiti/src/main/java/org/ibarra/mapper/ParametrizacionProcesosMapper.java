package org.ibarra.mapper;

import org.ibarra.dto.ParametrizacionProcesosDto;
import org.ibarra.entity.ParametrizacionProcesos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParametrizacionProcesosMapper {
    ParametrizacionProcesos toEntity(ParametrizacionProcesosDto dto);

    ParametrizacionProcesosDto toDto(ParametrizacionProcesos entity);
}
