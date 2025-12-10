package org.sas.seguridad.service;

import org.sas.seguridad.dto.UsuarioInicioSesion;
import org.sas.seguridad.entity.UsuarioClave;
import org.sas.seguridad.repository.UsuarioClaveRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsuarioClaveService {

    private static final Logger logger = Logger.getLogger(UsuarioClaveService.class.getName());

    @Autowired
    private UsuarioClaveRepo usuarioClaveRepo;
    @Autowired
    private EncryptDecrypt encryptDecrypt;

    public UsuarioClave find(UsuarioClave data) {
        return usuarioClaveRepo.findOne(Example.of(data)).orElse(null);
    }



    public void updatePassword(UsuarioInicioSesion data) {
        logger.log(Level.INFO, "update {0}", data.toString());
        UsuarioClave uc = find(new UsuarioClave(null, data.getId()));
        if (uc == null) {
            uc = new UsuarioClave();
            uc.setUsuario(data.getId());
            uc.setEstado(Boolean.TRUE);
            uc.setFecha(new Date());
        }
        if(uc.getFecha() == null){
            uc.setFecha(new Date());
        }
        uc.setClave(encryptDecrypt.encriptarTexto(data.getClave()));
        usuarioClaveRepo.save(uc);
    }
}
