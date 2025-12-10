package org.sas.administrativo.service.commons;

import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.entity.configuracion.SecuenciaGeneral;
import org.sas.administrativo.repository.SecuenciaGeneralRepository;
import org.sas.administrativo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SecuenciaGeneralService {
    private static final Logger LOG = Logger.getLogger(SecuenciaGeneralService.class.getName());
    @Autowired
    private SecuenciaGeneralRepository repository;

    /**
     * @param respuestaWs data: es el código de la secuencia
     * @return RespuestaWs: data: código de la secuencia - mensaje: valor secuencia - estado: ok secuencia
     */

    public RespuestaWs generarSecuencia(RespuestaWs respuestaWs) {
        try {
            Long secuencia = getSecuencia(respuestaWs.getData());
            respuestaWs.setMensaje(secuencia.toString());
            respuestaWs.setEstado(Boolean.TRUE);
            return respuestaWs;
        } catch (Exception e) {
            e.printStackTrace();
            respuestaWs.setEstado(Boolean.FALSE);
            return respuestaWs;
        }
    }

    public Long getSecuencia(String code) {
        System.out.println("----------> code "+code);
        try {
            Integer anio = Utils.getAnio(new Date());
            SecuenciaGeneral sc = repository.findByCodeAndAnio(code, anio);
            if (sc == null || sc.getId() == null) {
                sc = new SecuenciaGeneral(code, 0L);
                sc.setAnio(anio);
                sc = repository.save(sc);
            }
            sc.setSecuencia(sc.getSecuencia() + 1);
            sc = repository.save(sc);
            return sc.getSecuencia();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public Long getSecuencia(String code, Integer anio) {
        try {
            SecuenciaGeneral sc = repository.findByCodeAndAnio(code, anio);
            if (sc == null || sc.getId() == null) {
                sc = new SecuenciaGeneral(code, 0L);
                sc.setAnio(anio);
                sc = repository.save(sc);
            }
            if (sc.getSecuencia() == null) {
                sc.setSecuencia(0L);
            }
            sc.setSecuencia(sc.getSecuencia() + 1);
            sc = repository.save(sc);
            return sc.getSecuencia();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public SecuenciaGeneral getSecuenciaGeneral(String code, Integer anio) {
        try {
            SecuenciaGeneral sc = repository.findByCodeAndAnio(code, anio);
            if (sc == null || sc.getId() == null) {
                sc = new SecuenciaGeneral(code, 0L);
                sc.setAnio(anio);
                sc = repository.save(sc);
            }
            if (sc.getSecuencia() == null) {
                sc.setSecuencia(0L);
            }
            sc.setSecuencia(sc.getSecuencia() + 1);
            sc = repository.save(sc);
            return sc;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }


    public RespuestaWs getSecuenciaGeneralRecaudacion(String code, Integer anio) {
        try {
            SecuenciaGeneral sc = repository.findByCodeAndAnio(code, anio);
            if (sc == null || sc.getId() == null) {
                sc = new SecuenciaGeneral(code, 0L);
                sc.setAnio(anio);
                sc = repository.save(sc);
            }
            if (sc.getSecuencia() == null) {
                sc.setSecuencia(0L);
            }
            sc.setSecuencia(sc.getSecuencia() + 1);
            sc = repository.save(sc);
            return new RespuestaWs(Boolean.TRUE, null, sc.getSecuencia().toString());

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public Long getSecuenciaByCode(String code) {
        try {
            SecuenciaGeneral sc = repository.findByCode(code);
            if (sc == null || sc.getId() == null) {
                sc = new SecuenciaGeneral(code, 0L);
                sc = repository.save(sc);
            }
            sc.setSecuencia(sc.getSecuencia() + 1);
            sc = repository.save(sc);
            return sc.getSecuencia();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public Long findSecuenciaByCode(String code) {
        try {
            SecuenciaGeneral sc = repository.findByCode(code);
            if (sc == null || sc.getId() == null) {
                sc = new SecuenciaGeneral(code, 0L);
                sc = repository.save(sc);
            }
            sc.setSecuencia(sc.getSecuencia() + 1);
            return sc.getSecuencia();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public boolean getUpdateSecuenciaByCode(String code) {
        try {
            SecuenciaGeneral sc = repository.findByCode(code);
            sc.setSecuencia(sc.getSecuencia() + 1);
            save(sc);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return false;
        }
    }

    public SecuenciaGeneral save(SecuenciaGeneral data) {
        return repository.save(data);
    }

    public Long getSecuenciaByCode(String code, Integer anio) {
        try {
            SecuenciaGeneral sc = repository.findByCodeAndAnio(code, anio);
            return sc.getSecuencia();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

}
