package org.sas.seguridad.mapper;

import org.sas.seguridad.dto.UsuarioInicioSesion;
import org.sas.seguridad.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "persona", source = "entity.persona.id")
    UsuarioInicioSesion toDto(Usuario entity);

    @Mapping(target = "personaId", source = "dto.persona")
    @Mapping(target = "persona.id", source = "dto.persona")
    Usuario toEntity(UsuarioInicioSesion dto);
}
