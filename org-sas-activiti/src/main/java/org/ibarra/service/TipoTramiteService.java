package org.ibarra.service;

import org.ibarra.dto.RespuestaWs;
import org.ibarra.dto.TipoTramiteDto;
import org.ibarra.dto.TipoTramiteRequisitoDto;
import org.ibarra.entity.TipoTramite;
import org.ibarra.entity.TipoTramiteRequisito;
import org.ibarra.mapper.TipoTramiteMapper;
import org.ibarra.mapper.TipoTramiteRequisitoMapper;
import org.ibarra.repository.TipoTramiteRepo;
import org.ibarra.repository.TipoTramiteRequisitoRepo;
import org.ibarra.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TipoTramiteService {

    @Autowired
    private TipoTramiteRepo repository;
    @Autowired
    private TipoTramiteMapper tipoTramiteMapper;
    @Autowired
    private TipoTramiteRepo tipoTramiteRepo;
    @Autowired
    private TipoTramiteRequisitoRepo tipoTramiteRequisitoRepo;
    @Autowired
    private TipoTramiteRequisitoMapper tipoTramiteRequisitoMapper;

    public TipoTramiteDto consultar(TipoTramiteDto dto) {
        TipoTramite tipoTramite = tipoTramiteMapper.toEntity(dto);
        Optional<TipoTramite> tt = repository.findOne(Example.of(tipoTramite));
        if (tt.isPresent()) {
            return tipoTramiteMapper.toDto(tt.get());
        } else {
            return null;
        }
    }


    public List<TipoTramiteDto> findByTiposTramiteActivitiKey(String activitiKeys) {
        if (activitiKeys != null) {
            String[] activities = activitiKeys.split(",");
            List<TipoTramite> result = repository.findAllByActivitykeyInAndEstadoOrderByIdAsc(activities, Boolean.TRUE);
            if (result != null && result.size() > 0) {
                return result.stream().map(x -> tipoTramiteMapper.toDto(x)).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List<TipoTramiteDto> findAllByTipoTramites() {
        List<TipoTramite> result = repository.findAllByEstadoOrderByIdDesc(Boolean.TRUE);
        if (result != null && result.size() > 0) {
            return result.stream().map(x -> tipoTramiteMapper.toDto(x)).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }


    public RespuestaWs guardarTipoTramite(TipoTramiteDto tipoTramiteDto) {
        try {
            System.out.println("tipo tramite dto:" + tipoTramiteDto.toString());
            TipoTramite entity = tipoTramiteMapper.toEntity(tipoTramiteDto);
            entity = tipoTramiteRepo.save(entity);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
        } catch (Exception ex) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);

        }
    }

    public RespuestaWs guardarTipoTramiteRequisito(TipoTramiteRequisitoDto tipoTramiteRequisitoDto) {
        try {
            TipoTramiteRequisito entity = tipoTramiteRequisitoMapper.toEntity(tipoTramiteRequisitoDto);
            entity = tipoTramiteRequisitoRepo.save(entity);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.datosCorrecto);
        } catch (Exception ex) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

}
