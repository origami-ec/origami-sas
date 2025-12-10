package org.sas.administrativo.mapper;

import org.mapstruct.Mapper;
import org.sas.administrativo.dto.commons.PersonaDto;
import org.sas.administrativo.entity.Persona;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    Persona toEntity(PersonaDto dto);

    PersonaDto toDto(Persona entity);
}
