package org.ibarra.mapper;


import org.ibarra.dto.DocumentoTramiteDto;
import org.ibarra.entity.DocumentoTramite;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {HistoricoTramiteMapper.class})
public interface DocumentoTramiteMapper {
    DocumentoTramite toEntity(DocumentoTramiteDto dto);

    DocumentoTramiteDto toDto(DocumentoTramite entity);

    List<DocumentoTramiteDto> toDto(List<DocumentoTramite> entity);

    List<DocumentoTramite> toEntity(List<DocumentoTramiteDto> dto);
}
