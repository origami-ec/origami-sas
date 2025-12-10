package org.sas.administrativo.mapper.commons;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.sas.administrativo.dto.commons.CatalogoItemDto;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",uses ={CatalogoMapper.class} )
@Component
public interface CatalogoItemMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "codigo", source = "codigo"),
            @Mapping(target = "descripcion", source = "descripcion"),
            //@Mapping(target = "defaultValue", source = "defaultValue"),
            //@Mapping(target = "estado", source = "estado"),
            @Mapping(target = "orden", source = "orden"),
            //@Mapping(target = "porcentaje", source = "porcentaje"),
            //@Mapping(target = "porcentajeRetencion", source = "porcentajeRetencion"),
            //@Mapping(target = "porcentajeLibre", source = "porcentajeLibre"),
            //@Mapping(target = "tipoImpuesto", source = "tipoImpuesto"),
            @Mapping(target = "texto", source = "texto"),
            //@Mapping(target = "padre", source = "padre"),
            //@Mapping(target = "referencia", source = "referencia"),
            //@Mapping(target = "valor", source = "valor")
    })
    CatalogoItemDto toDto(CatalogoItem entity);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "codigo", source = "codigo"),
            @Mapping(target = "descripcion", source = "descripcion"),
            //@Mapping(target = "defaultValue", source = "defaultValue"),
            //@Mapping(target = "estado", source = "estado"),
            @Mapping(target = "orden", source = "orden"),
            //@Mapping(target = "porcentaje", source = "porcentaje"),
            //@Mapping(target = "porcentajeRetencion", source = "porcentajeRetencion"),
            //@Mapping(target = "porcentajeLibre", source = "porcentajeLibre"),
            //@Mapping(target = "tipoImpuesto", source = "tipoImpuesto"),
            @Mapping(target = "texto", source = "texto"),
            //@Mapping(target = "padre", source = "padre"),
            //@Mapping(target = "referencia", source = "referencia"),
            //@Mapping(target = "valor", source = "valor")
    })
    CatalogoItem toEntity(CatalogoItemDto dto);
}
