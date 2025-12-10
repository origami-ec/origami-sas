package org.ibarra.service;

import org.ibarra.repository.PersistableRepository;
import org.ibarra.util.Utils;
import org.ibarra.util.model.BusquedaDinamica;
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
            List rs = repository.findAllDinamic(data);
            if (Utils.isEmpty(rs)) {
                return null;
            }
            if (data.getUnicoResultado()) {
                return rs.get(0);
            } else {
                return rs;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    public Object findAllDinamic(String nameClazz, BusquedaDinamica data, MultiValueMap<String, String> headers) {
        try {
            return repository.findAllDinamic(data, headers);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }
}
