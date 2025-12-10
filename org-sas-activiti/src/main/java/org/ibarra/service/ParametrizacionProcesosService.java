package org.ibarra.service;

import lombok.extern.slf4j.Slf4j;
import org.ibarra.dto.ParametrizacionProcesosDto;
import org.ibarra.dto.RespuestaWs;
import org.ibarra.entity.ParametrizacionProcesos;
import org.ibarra.mapper.ParametrizacionProcesosMapper;
import org.ibarra.repository.ParametrizacionProcesosRepo;
import org.ibarra.util.Constantes;
import org.ibarra.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParametrizacionProcesosService {
    @Autowired
    private ParametrizacionProcesosRepo repo;
    @Autowired
    private ParametrizacionProcesosMapper mapper;

    private ParametrizacionProcesos entity;

    public RespuestaWs saveParametrizacion(ParametrizacionProcesosDto dto) {
        try {
            System.out.println("datos -> {}" + dto.toString());
            entity = new ParametrizacionProcesos();
            entity = mapper.toEntity(dto);
            entity = repo.save(entity);
            dto = mapper.toDto(entity);
            return new RespuestaWs(Boolean.TRUE, Utils.toJson(dto), Constantes.datosCorrecto);
        } catch (Exception e) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    public RespuestaWs eliminarParametrizacion(ParametrizacionProcesosDto dto) {
        try {
            entity = new ParametrizacionProcesos();
            entity = mapper.toEntity(dto);
            repo.delete(entity);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
        } catch (Exception e) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    public ParametrizacionProcesosDto findByCamposParametrizacionProcesos(ParametrizacionProcesosDto dto) {
        System.out.println("pilas>>" + dto.toString());
        try {
            dto.setValidarFirma(null);
            entity = new ParametrizacionProcesos();
            entity = mapper.toEntity(dto);
            entity = repo.findOne(Example.of(entity)).get();
            System.out.println("entidad parametros {}" + entity.toString());
            if (entity != null && entity.getId() != null) {
                return mapper.toDto(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}