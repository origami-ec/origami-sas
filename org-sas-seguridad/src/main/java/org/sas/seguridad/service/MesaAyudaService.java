package org.sas.seguridad.service;

import org.sas.seguridad.conf.AppProps;
import org.sas.seguridad.dto.MesaAyudaDto;
import org.sas.seguridad.dto.RespuestaWs;
import org.sas.seguridad.entity.MesaAyuda;
import org.sas.seguridad.mapper.MesaAyudaMapper;
import org.sas.seguridad.repository.MesaAyudaRepository;
import org.sas.seguridad.service.commons.RestService;
import org.sas.seguridad.util.Constantes;
import org.sas.seguridad.util.Utils;
import org.sas.seguridad.util.ValoresCodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MesaAyudaService {
    private final Logger log = Logger.getLogger(MesaAyudaService.class.getName());
    private static final Logger logger = Logger.getLogger(MesaAyudaService.class.getName());
    @Autowired
    private MesaAyudaRepository mesaAyudaRepo;
    @Autowired
    private MesaAyudaMapper mesaAyudaMapper;
    @Autowired
    private RestService restService;
    @Autowired
    private AppProps appProps;

    public MesaAyudaService() {
    }

    public MesaAyudaDto save(MesaAyudaDto dto) {
        try {
            MesaAyuda mesa = this.mesaAyudaMapper.toEntity(dto);
            if (mesa.getEstado().contains("GENERADO") && Utils.isEmptyString(mesa.getCodigo())) {
                String valor = this.restService.secuencia(ValoresCodigo.SECUENCIA_TICKET);
                if (valor != null && !valor.isEmpty()) {
                    Integer numeracion = Integer.parseInt(valor);
                    mesa.setNumeracion(numeracion);
                    String var10001 = Utils.completarCadenaConCeros(valor, 4);
                    mesa.setCodigo(var10001 + "-" + Utils.getAnio(new Date()));
                    mesa.setPeriodo(Utils.getAnio(new Date()));
                }
            }

            mesa = this.mesaAyudaRepo.save(mesa);
            MesaAyudaDto mesaAyudaDto = this.mesaAyudaMapper.toDto(mesa);
            return mesaAyudaDto;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public RespuestaWs getMaxMesaAyuda() {
        RespuestaWs resp = new RespuestaWs();

        try {
            Integer dataRes = this.mesaAyudaRepo.getMaxMesaAyuda();
            resp.setEstado(true);
            resp.setMensaje(Constantes.datosCorrecto);
            resp.setData(Utils.toJson(dataRes));
        } catch (Exception var3) {
            logger.log(Level.SEVERE, "", var3);
            resp.setEstado(false);
            resp.setMensaje(Constantes.intenteNuevamente);
        }

        return resp;
    }

    public List<MesaAyudaDto> findAllMesaAyuda() {
        new ArrayList();
        List<MesaAyudaDto> dto = new ArrayList();

        try {
            List<MesaAyuda> mesaAyudas = this.mesaAyudaRepo.findAll();
            dto = this.mesaAyudaMapper.toDto(mesaAyudas);
        } catch (Exception var4) {
            this.log.log(Level.SEVERE, "", var4);
        }

        return (List)dto;
    }

    public MesaAyudaDto findMesaAyuda(Long id) {
        new MesaAyudaDto();
        MesaAyudaDto dto = this.mesaAyudaMapper.toDto(this.mesaAyudaRepo.findMesaAyudaById(id));
        return dto;
    }
}