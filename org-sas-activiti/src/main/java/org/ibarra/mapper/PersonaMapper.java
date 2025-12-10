package org.ibarra.mapper;

import org.ibarra.dto.PersonaDto;
import org.ibarra.entity.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring" )
public interface PersonaMapper {

    PersonaDto toDto(Persona persona);

}
