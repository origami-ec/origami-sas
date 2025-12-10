package org.sas.administrativo.mapper.talentohumano;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sas.administrativo.dto.ServidorDatos;
import org.sas.administrativo.entity.talentohumano.Servidor;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServidorDatosMapper {


    Servidor toEntity(ServidorDatos dto);

    @Mapping( target = "servidor", expression = "java(entity.getPersona() != null ?  (entity.getPersona().getNombre() + \" \" + entity.getPersona().getApellido()) : null )")
    @Mapping(target = "identificacion", source = "entity.persona.numIdentificacion")
    @Mapping(target = "titulo", source = "entity.persona.tituloProfesional")
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "nombres", source = "entity.persona.nombre")
    @Mapping(target = "apellidos", source = "entity.persona.apellido")
    @Mapping(target = "personaID", source = "entity.persona.id")
    @Mapping(target = "correoPersonal", source = "entity.persona.correo")
    @Mapping(target = "correoInstitucional", source = "entity.emailInstitucion")
    ServidorDatos toDto(Servidor entity);

    List<ServidorDatos> toDto(List<Servidor> entity);

    List<Servidor> toEntity(List<ServidorDatos> dto);
}
