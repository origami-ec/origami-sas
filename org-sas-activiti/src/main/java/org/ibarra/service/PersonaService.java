package org.ibarra.service;

import org.ibarra.conf.AppProps;
import org.ibarra.dto.PersonaDto;
import org.ibarra.dto.Usuario;
import org.ibarra.dto.UsuarioDetalle;
import org.ibarra.entity.Persona;
import org.ibarra.mapper.PersonaMapper;
import org.ibarra.repository.PersonaRepository;
import org.ibarra.util.Constantes;
import org.ibarra.util.model.BusquedaDinamica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {
    @Autowired
    private RestService restService;
    @Autowired
    private AppProps appProps;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PersonaMapper personaMapper;

    public PersonaDto consultarXid(Long id) {
        Persona p = personaRepository.getReferenceById(id);
        return personaMapper.toDto(p);
    }

    public Persona getPersonaByUsuario(String usuario) {
        try {
            BusquedaDinamica b = new BusquedaDinamica("Usuario");
            b.addFilter("estado", "ACTIVO");
            b.addFilter("usuario", usuario);
            List<Usuario> result = restService.restPOSTList(appProps.getUrlSeguridad().concat("busquedas/findBy"), b, Usuario[].class);
            if (result != null) {
                if (result.get(0) != null) {
                    Usuario u = result.get(0);
                    if (u.getPersona() != null) return u.getPersona();
                    Persona p = personaRepository.findByIdAndEstado(u.getPersonaId(), true);
                    return p;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UsuarioDetalle getUsuario(String usuario) {
        try {
            if (usuario.equals(Constantes.PORTAL_WEB)) return null;
            UsuarioDetalle result = (UsuarioDetalle) restService.restPOST(appProps.getUrlSeguridad().concat("consultar/usuario/").concat(usuario), null, null, UsuarioDetalle.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
