package org.sas.administrativo.mapper.commons;

import org.sas.administrativo.dto.commons.FirmaDocumentoDto;
import org.sas.administrativo.entity.FirmaDocumento;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses ={CatalogoItemMapper.class} )
public interface FirmaDocumentoMapper {

    FirmaDocumento toEntity(FirmaDocumentoDto dto);

    FirmaDocumentoDto toDto(FirmaDocumento entity);

    List<FirmaDocumentoDto> toDto(List<FirmaDocumento> entity);

    List<FirmaDocumento> toEntity(List<FirmaDocumentoDto> entity);

}
