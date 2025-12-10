package org.sas.seguridad.mapper;

import org.sas.seguridad.dto.UsuarioVerificacionDTO;
import org.sas.seguridad.entity.UsuarioVerificacion;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface UsuarioVerificacionMapper {

    UsuarioVerificacionDTO toDTO(UsuarioVerificacion entity);

    UsuarioVerificacion toEntity(UsuarioVerificacionDTO dto);
}
