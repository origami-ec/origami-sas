package org.sas.zull.service;

import org.sas.zull.conf.AppProps;
import org.sas.zull.entity.Usuario;
import org.sas.zull.entity.UsuarioAcceso;
import org.sas.zull.repository.UsuarioAccesoRepository;
import org.sas.zull.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsuarioAcessoService {
    @Autowired
    private AppProps appProps;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioAccesoRepository repository;



    public UsuarioAcceso guardarAcceso(Long usuario, String ip, String os, String mac, String estado) {

        Integer intentos = 1;
        Long id = null;
        UsuarioAcceso accesoBD = repository.findByUsuarioAndEstado(usuario, Constantes.FALLIDO);
        if (accesoBD != null) {
            id = accesoBD.getId();
            intentos = accesoBD.getIntento() + 1;
            if (intentos >= appProps.getIntentosSesion()) {
                if (!Constantes.CORRECTO.equals(estado)) {
                    estado = appProps.getUsuarioBloqueado();
                    Usuario u = usuarioService.findById(usuario);
                    u.setEstado(estado);
                    usuarioService.actualizar(u);
                }
            }
        }

        UsuarioAcceso usuarioAcceso = new UsuarioAcceso();
        usuarioAcceso.setId(id);
        usuarioAcceso.setUsuario(usuario);
        usuarioAcceso.setIp(ip);
        usuarioAcceso.setMac(mac);
        usuarioAcceso.setOs(os);
        usuarioAcceso.setFecha(new Date());
        usuarioAcceso.setEstado(estado);
        usuarioAcceso.setIntento(intentos);
        return repository.save(usuarioAcceso);

    }


}

