package org.sas.seguridad.service;

import org.sas.seguridad.entity.CorreoFormato;
import org.sas.seguridad.repository.CorreoFormatoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorreoFormatoService {

    @Autowired
    private CorreoFormatoRepo correoFormatoRepo;

    public CorreoFormato getCorreoFormato(String codigo) {
        return correoFormatoRepo.findByCodigoAndEstado(codigo, Boolean.TRUE);
    }

}
