package org.sas.seguridad.service;

import com.google.gson.Gson;
import org.sas.seguridad.conf.AppProps;
import org.sas.seguridad.dto.Persona;
import org.sas.seguridad.dto.RespuestaWs;
import org.sas.seguridad.dto.ServidorDatos;
import org.sas.seguridad.entity.PersonaFD;
import org.sas.seguridad.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AdministrativoService {

    private Logger LOG = Logger.getLogger(AdministrativoService.class.getName());
    @Autowired
    private AppProps appProps;
    @Autowired
    private RemoteService remoteService;

    @Autowired
    private PersonaRepository personaRepository;

    public PersonaFD find(PersonaFD persona) {
        return personaRepository.findOne(Example.of(persona)).orElse(null);
    }

    /**
     * Finds and retrieves the server data from the given response object.
     *
     * @param datos the response object containing the data required for retrieving the server information
     * @return the server data as a ServidorDatos object if successfully retrieved; otherwise, null
     */

    public ServidorDatos findServidorDatos(RespuestaWs datos) {
        String url = appProps.getAdministrativo() + "servidor/informacionXpersona";
        try {
            RespuestaWs respuestaWs = (RespuestaWs) remoteService.restPOST(url, null, datos, RespuestaWs.class);
            //System.out.println("respuestaWs " + respuestaWs.getData());
            if (respuestaWs != null && respuestaWs.getEstado()) {
                ServidorDatos sd = new Gson().fromJson(respuestaWs.getData(), ServidorDatos.class);
                return sd;
            } else {
                return null;
            }
        } catch (ResourceAccessException e) {
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busqueda persona: " + url, e);
            return null;
        }
    }


}
