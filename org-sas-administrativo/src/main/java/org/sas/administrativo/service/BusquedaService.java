package org.sas.administrativo.service;


import org.sas.administrativo.repository.PersistableRepository;
import org.sas.administrativo.util.GuardarModel;
import org.sas.administrativo.util.Utils;
import org.sas.administrativo.util.model.BusquedaDinamica;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class BusquedaService {

    private final PersistableRepository repository;
    private final Logger LOG = Logger.getLogger(BusquedaService.class.getName());

    public BusquedaService(PersistableRepository repository) {
        this.repository = repository;
    }

    public Object findAllDinamic(String nameClazz, BusquedaDinamica data) {
        try {
            List rs = repository.findAllDinamic(nameClazz, data);
            if (Utils.isNotEmpty(rs)) {
                if (data.getUnicoResultado()) {
                    if (data.getGson()) {
                        return Utils.toObjecttoHashMap(rs.get(0));
                    } else {
                        return rs.get(0);
                    }
                } else {
                    if (data.getGson()) {
                        return Utils.toObjecttoHashMap(rs);
                    } else {
                        return rs;
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public Object findAllDinamic(String nameClazz, BusquedaDinamica data, MultiValueMap<String, String> headers) {
        try {
            List<Object> allDinamic = repository.findAllDinamic(nameClazz, data, headers);
            if (data.getGson()) {
                return Utils.toObjecttoHashMap(allDinamic);
            } else {
                return allDinamic;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public Object findAllFunction(BusquedaDinamica data, MultiValueMap<String, String> headers) {
        try {
            return repository.findAllFunction(data, headers);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public Object saveEntiti(GuardarModel data) {
        try {
            return repository.save(data);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }


    public Boolean existe(BusquedaDinamica data) {
        try {
            boolean exists = repository.exists(data);
            return exists;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return false;
    }
}
