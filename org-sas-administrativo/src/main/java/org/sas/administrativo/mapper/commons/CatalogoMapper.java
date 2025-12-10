package org.sas.administrativo.mapper.commons;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.sas.administrativo.dto.commons.CatalogoDto;
import org.sas.administrativo.entity.configuracion.Catalogo;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CatalogoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "codigo", source = "codigo"),
            @Mapping(target = "nombre", source = "nombre"),
            @Mapping(target = "descripcion", source = "descripcion"),
            @Mapping(target = "estado", source = "estado")
    })
    CatalogoDto toDto(Catalogo entity);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "codigo", source = "codigo"),
            @Mapping(target = "nombre", source = "nombre"),
            @Mapping(target = "descripcion", source = "descripcion"),
            @Mapping(target = "estado", source = "estado")
    })
    Catalogo toEntity(CatalogoDto dto);
}
