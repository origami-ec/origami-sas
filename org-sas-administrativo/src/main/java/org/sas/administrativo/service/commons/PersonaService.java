package org.sas.administrativo.service.commons;

import org.sas.administrativo.conf.AppProps;
import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.commons.PersonaDto;
import org.sas.administrativo.entity.Canton;
import org.sas.administrativo.entity.Parroquia;
import org.sas.administrativo.entity.Persona;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.entity.talentohumano.PersonaActividad;
import org.sas.administrativo.mapper.PersonaMapper;
import org.sas.administrativo.repository.PersonaRepository;
import org.sas.administrativo.repository.configuracion.ParroquiaRepository;
import org.sas.administrativo.repository.talentohumano.PersonaActividadRepository;
import org.sas.administrativo.service.configuracion.CantonService;
import org.sas.administrativo.util.Constantes;
import org.sas.administrativo.util.Utils;
import org.sas.administrativo.util.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.NonUniqueResultException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;

@Service
public class PersonaService {

    @Autowired
    private AppProps appProps;
    @Autowired
    private PersonaRepository repository;
    @Autowired
    private PersonaActividadRepository personaActividadRepository;
    @Autowired
    private PersonaMapper personaMapper;

    public Persona consultarSolicitante(Long id) {
        Persona result = null;
        try {
            result = repository.findByIdPersona(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Persona consultar(Persona persona) {
        System.out.println("si entro al metodo de buscar persona");
        Persona result = null;
        try {
            //log.info("persona: " + persona.toString());
            result = consultarPersonaResult(persona);
            //result = null;//repository.findOne(Example.of(persona)).orElse(null);
            if (result == null || result.getId() == null) {
                System.out.println("primer if: ");
                if (persona.getNumIdentificacion() != null) {
                    if (persona.getNumIdentificacion().trim().length() > 5) {
                        result = this.consultaByDinardapInter(persona);
                    }
                }
            } else {
                Persona pDinardap = this.consultaByDinardapSinGuardar(result);
                if (pDinardap != null) {
                    result = this.actualizarDatosDinardap(result);
                }
                if (result != null && Utils.isNotEmptyString(result.getNumIdentificacion())) {
                    if (result.getNumIdentificacion().trim().length() == 10) {
                        if ((result.getNombre() == null || result.getNombre().length() == 0) &&
                                (result.getApellido() == null || result.getApellido().length() == 0)) {
                            System.out.println("//////// Actualizacion de datos de personas");
                            result = this.actualizarDatosDinardap(result);
                        }
                    } else {
                        if ((result.getNombre() == null || result.getNombre().length() == 0)) {
                            System.out.println("//////// Actualizacion de datos de otros");
                            result = this.actualizarDatosDinardap(result);
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("result: " + result.getId());
//        System.out.println("getNombreCompleto: " + result.getNombreCompleto());
        return result;
    }

    public Persona consultarPersonaResult(Persona persona) {
        Persona result = null;
        try {
            Persona per;
            if (persona.getNumIdentificacion() != null) {
                if (persona.getId() != null) {
                    per = new Persona(persona.getId(), persona.getNumIdentificacion());
                } else {
                    per = new Persona(persona.getNumIdentificacion());
                }
            } else {
                per = new Persona(persona.getId());
            }
            result = repository.findOne(Example.of(per)).orElse(null);
        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException e) {
            System.out.println("Buscando el ultimo ingresado existe mas de uno " + persona.getNumIdentificacion());
            result = repository.findTop1ByNumIdentificacionOrderByIdDesc(persona.getNumIdentificacion());
        }
        return result;
    }

    public Persona actualizarDatosDinardap(Persona persona) {
        if (persona.getId() == null) {
            return persona;
        }
        Persona result = this.repository.findById(persona.getId()).orElse(null);
        try {
            result = this.consultaByDinardapInter(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Persona consultaByDinardapInter(Persona persona) {
        try {
            if (persona.getNumIdentificacion() != null) {
                if (persona.getNumIdentificacion().length() == 0) {
                    return null;
                }
                Persona perTemp = repository.findTop1ByNumIdentificacionOrderByIdDesc(persona.getNumIdentificacion());
                if (perTemp != null && perTemp.getId() != null) {
                    persona = perTemp;
                }
                BusquedaDinardap bd = new BusquedaDinardap();
                bd.setKey(appProps.getDinardapKey());
                ArrayList<BusquedaDinardapParametro> pm = new ArrayList();
                if (persona.getNumIdentificacion().length() == 13) {
                    pm.add(new BusquedaDinardapParametro("codigoPaquete", appProps.getDinardapCodigoPaqueteSri()));
                } else {
                    pm.add(new BusquedaDinardapParametro("codigoPaquete", appProps.getDinardapCodigoPaquetePer()));
                }
                pm.add(new BusquedaDinardapParametro("identificacion", persona.getNumIdentificacion()));
                pm.add(new BusquedaDinardapParametro("fuenteDatos", (persona.getNumIdentificacion().length() == 10) ? "CED" : (persona.getNumIdentificacion().length() == 13 ? "RUC" : "PAS")));
                bd.setParametros(pm);
                RestTemplate restTemplate = new RestTemplate();

                URI uri = new URI(appProps.getDinardapIp().concat(appProps.getDinardapPathConsulta()));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String json = Utils.toJson(bd);
                HttpEntity<Object> entity = new HttpEntity<>(json, headers);
                System.out.println("Uri: " + uri + " --> " + entity);
                ResponseEntity<DinardapRespuesta> pers = restTemplate.exchange(uri, HttpMethod.POST, entity, DinardapRespuesta.class);
                if (pers != null && pers.getBody() != null) {
                    DinardapRespuesta respuesta = pers.getBody();
                    if(respuesta !=null){
                    System.out.println("--> Respuesta dinardap: " + respuesta);
                    Persona prsn = new Persona();
                    PersonaActividad actividad = new PersonaActividad();
                    if (persona.getId() != null) {
                        prsn = persona;
                    } else {
                        prsn.setFechaCreacion(new Date());
                        prsn.setEstado(true);
                    }
                    // PERSONA_TIPO_DOC --> tipoDoc

                    for (DinardapEntidad de : respuesta.getInteroperadorpaquete().getEntidades()) {
                        System.out.println("Entidad --> " + de.getNombre());
                        for (DinardapFila df : de.getFilas()) {
                            System.out.println("    Fila --> " + df.getColumnas());
                            prsn.setDomicilio(null);
                            for (BusquedaDinardapParametro c : df.getColumnas()) {
                                System.out.println("        Campo --> " + c.getCampo() + " --> " + c.getValor());
                                if (de.getNombre().equalsIgnoreCase("Contribuyente Datos Completo")) {

                                    if (c.getCampo().equalsIgnoreCase("email")) {
                                        prsn.setCorreo(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("nombreComercial")) {
                                        prsn.setApellido(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("numeroRuc")) {
                                        prsn.setNumIdentificacion(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("razonSocial")) {
                                        prsn.setNombre(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("telefonoDomicilio")) {
                                        prsn.setTelefono(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("fechaInicioActividades")) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            prsn.setFechaNacimiento(sdf.parse(c.getValor()));
                                            prsn.setFechaExpedicion(sdf.parse(c.getValor()));
                                        } catch (Exception e) {
                                            System.out.println(" fechaInicioActividades --> " + e.getMessage());
                                        }
                                    }
                                } else if (de.getNombre().equalsIgnoreCase("Datos Demográficos (Registro Civil)")) {
                                    prsn.setNumIdentificacion(persona.getNumIdentificacion());
                                    if (c.getCampo().equalsIgnoreCase("callesDomicilio")) {
                                        if (prsn.getDomicilio() == null) {
                                            prsn.setDomicilio(c.getValor());
                                        } else {
                                            prsn.setDomicilio(prsn.getDomicilio().concat(" ").concat(c.getValor()));
                                        }
                                    }
                                    if (c.getCampo().equalsIgnoreCase("domicilio")) {
                                        if (prsn.getDomicilio() == null) {
                                            prsn.setDomicilio(c.getValor());
                                        } else {
                                            prsn.setDomicilio(c.getValor().concat(" ").concat(prsn.getDomicilio()));
                                        }
                                    }
                                    if (c.getCampo().equalsIgnoreCase("condicionCiudadano")) {
                                        prsn.setCondicionCiudadano(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("estadoCivil")) {
                                        prsn.setEstadoCivil(c.getValor());
                                    }

                                    if (c.getCampo().equalsIgnoreCase("fechaExpedicion")) {
                                        try {
                                            if (c.getValor() == null || c.getValor() == "") {

                                            } else {
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                prsn.setFechaExpedicion(sdf.parse(c.getValor()));
                                            }
                                        } catch (Exception e) {
                                            System.out.println(" fechaExpedicion --> " + e.getMessage());
                                        }
                                    }
                                    if (c.getCampo().equalsIgnoreCase("fechaNacimiento")) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                            prsn.setFechaNacimiento(sdf.parse(c.getValor()));
                                        } catch (Exception e) {
                                            System.out.println(" fechaNacimiento --> " + e.getMessage());
                                        }
                                    }

                                    if (c.getCampo().equalsIgnoreCase("individualDactilar") || c.getCampo().equalsIgnoreCase("codigo")) {
                                        prsn.setCodigo(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("nombre")) {
                                        String[] s = c.getValor().split(" ");
                                        if (s.length == 6) {
                                            prsn.setApellido(s[0] + " " + s[1] + " " + s[2]);
                                            prsn.setNombre(s[3] + " " + s[4] + " " + s[5]);
                                        } else if (s.length == 5) {
                                            prsn.setApellido(s[0] + " " + s[1] + " " + s[2]);
                                            prsn.setNombre(s[3] + " " + s[4]);
                                        } else if (s.length == 4) {
                                            prsn.setApellido(s[0] + " " + s[1]);
                                            prsn.setNombre(s[2] + " " + s[3]);
                                        } else if (s.length == 3) {
                                            prsn.setApellido(s[0] + " " + s[1]);
                                            prsn.setNombre(s[2]);
                                        } else if (s.length == 2) {
                                            prsn.setApellido(s[0]);
                                            prsn.setNombre(s[1]);
                                        } else {
                                            prsn.setNombre(c.getValor());
                                        }
                                    }
                                } else if ("Actividad Economica".equalsIgnoreCase(de.getNombre())) {
                                    if (c.getCampo().equalsIgnoreCase("actividadGeneral")) {
                                        actividad.setActividadGeneral(c.getValor());
                                    }
                                } else if ("Contador".equalsIgnoreCase(de.getNombre())) {
                                    if (c.getCampo().equalsIgnoreCase("cedulaContador")) {
                                        actividad.setCedulaContador(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("nombreContador")) {
                                        actividad.setNombreContador(c.getValor());
                                    }
                                } else if ("Estructura Organizacional".equalsIgnoreCase(de.getNombre())) {
                                    if (c.getCampo().equalsIgnoreCase("nombreProvincia")) {
                                        actividad.setNombreProvincia(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("nombreRegional")) {
                                        actividad.setNombreRegional(c.getValor());
                                    }
                                } else if ("Representante Legal".equalsIgnoreCase(de.getNombre())) {
                                    if (c.getCampo().equalsIgnoreCase("cargo")) {
                                        actividad.setCargo(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("identificacion")) {
                                        actividad.setIdentificacion(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("nombre")) {
                                        actividad.setNombre(c.getValor());
                                    }
                                }
// Fin personas
                                // Generales
                                if (de.getNombre().equalsIgnoreCase("Representante Legal")) {
                                    if (c.getCampo().equalsIgnoreCase("nombre")) {
                                        prsn.setRepresentanteLegal(c.getValor());
                                    }

                                }

                                if (de.getNombre().equalsIgnoreCase("Tipo Contribuyente")) {
                                    System.out.println("    --> " + c.getCampo() + ": " + c.getValor());
                                }


                            }

                        }
                    }
                    Persona personaBD = null;
                    if (persona.getId() == null) {
                        try {
                            personaBD = repository.findOne(Example.of(persona)).orElse(null);
                        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException e) {
                            System.out.println("Buscando el ultimo ingresado existe mas de uno " + persona.getNumIdentificacion());
                            personaBD = repository.findTop1ByNumIdentificacionOrderByIdDesc(persona.getNumIdentificacion());
                        }
                    } else {
                        personaBD = persona;
                    }
                    System.out.println("Verificacion " + personaBD);
                    if (personaBD != null) {
                        prsn.setId(personaBD.getId());
                    } else {
                        if (persona != null && persona.getId() != null) {
                            prsn.setId(persona.getId());
                        }
                    }
                    if (Utils.isNotEmptyString(prsn.getNumIdentificacion())) {
                        if (prsn.getFechaCreacion() == null) {
                            prsn.setFechaCreacion(new Date());
                        }
                        prsn = repository.save(prsn);
                        if (Utils.isNotEmptyString(actividad.getActividadGeneral())) {
                            actividad.setPersona(prsn.getId());
                            personaActividadRepository.save(actividad);
                        }

                    }

                    //prsn = repository.findOne(Example.of(new Persona(prsn.getId()))).orElse(null);
                    if (persona.getNumIdentificacion().equals(prsn.getNumIdentificacion())) {
                        return prsn;
                    } else {
                        return null;
                    }
                }
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Persona consultaByDinardapSinGuardar(Persona persona) {
        try {
            if (persona.getNumIdentificacion() != null) {
                if (persona.getNumIdentificacion().isEmpty()) {
                    return null;
                }
                BusquedaDinardap bd = new BusquedaDinardap();
                bd.setKey(appProps.getDinardapKey());
                ArrayList<BusquedaDinardapParametro> pm = new ArrayList();
                if (persona.getNumIdentificacion().length() == 13) {
                    pm.add(new BusquedaDinardapParametro("codigoPaquete", appProps.getDinardapCodigoPaqueteSri()));
                } else {
                    pm.add(new BusquedaDinardapParametro("codigoPaquete", appProps.getDinardapCodigoPaquetePer()));
                }
                pm.add(new BusquedaDinardapParametro("identificacion", persona.getNumIdentificacion()));
                pm.add(new BusquedaDinardapParametro("fuenteDatos", (persona.getNumIdentificacion().length() == 10) ? "CED" : (persona.getNumIdentificacion().length() == 13 ? "RUC" : "PAS")));
                bd.setParametros(pm);
                RestTemplate restTemplate = new RestTemplate(/*Utils.getClientHttpRequestFactory(appProps.getUserDinardap(), appProps.getPassDianardad())*/);
                URI uri = new URI(appProps.getDinardapIp().concat(appProps.getDinardapPathConsulta()));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String json = Utils.toJson(bd);
                HttpEntity<Object> entity = new HttpEntity<>(json, headers);
                System.out.println("Uri: " + uri + " --> " + entity);
                ResponseEntity<DinardapRespuesta> pers = restTemplate.exchange(uri, HttpMethod.POST, entity, DinardapRespuesta.class);
                if (pers != null && pers.getBody() != null) {
                    DinardapRespuesta respuesta = pers.getBody();
                    System.out.println("--> Respuesta dinardap: " + respuesta);
                    Persona prsn = new Persona();
                    if (persona.getId() != null) {
                        prsn = persona;
                    } else {
                        prsn.setFechaCreacion(new Date());
                        prsn.setEstado(true);
                    }
                    // PERSONA_TIPO_DOC --> tipoDoc

                    for (DinardapEntidad de : respuesta.getInteroperadorpaquete().getEntidades()) {
                        System.out.println("Entidad --> " + de.getNombre());
                        for (DinardapFila df : de.getFilas()) {
                            System.out.println("    Fila --> " + df.getColumnas());
                            prsn.setDomicilio(null);
                            for (BusquedaDinardapParametro c : df.getColumnas()) {
                                if (de.getNombre().equalsIgnoreCase("Contribuyente Datos Completo")) {
                                    if (c.getCampo().equalsIgnoreCase("direccionCorta")) {
                                        prsn.setDomicilio(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("email")) {
                                        prsn.setCorreo(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("nombreComercial")) {
                                        prsn.setApellido(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("numeroRuc")) {
                                        prsn.setNumIdentificacion(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("razonSocial")) {
                                        prsn.setNombre(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("telefonoDomicilio")) {
                                        prsn.setTelefono(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("fechaInicioActividades")) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            prsn.setFechaNacimiento(sdf.parse(c.getValor()));
                                            prsn.setFechaExpedicion(sdf.parse(c.getValor()));
                                        } catch (Exception e) {
                                            System.out.println(" fechaInicioActividades --> " + e.getMessage());
                                        }
                                    }
                                } else if (de.getNombre().equalsIgnoreCase("Datos Demográficos (Registro Civil)")) {
                                    prsn.setNumIdentificacion(persona.getNumIdentificacion());
                                    if (c.getCampo().equalsIgnoreCase("callesDomicilio")) {
                                        if (prsn.getDomicilio() == null) {
                                            prsn.setDomicilio(c.getValor());
                                        } else {
                                            prsn.setDomicilio(prsn.getDomicilio().concat(" ").concat(c.getValor()));
                                        }
                                    }
                                    if (c.getCampo().equalsIgnoreCase("domicilio")) {
                                        if (prsn.getDomicilio() == null) {
                                            prsn.setDomicilio(c.getValor());
                                        } else {
                                            prsn.setDomicilio(c.getValor().concat(" ").concat(prsn.getDomicilio()));
                                        }
                                    }
                                    if (c.getCampo().equalsIgnoreCase("condicionCiudadano")) {
                                        prsn.setCondicionCiudadano(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("estadoCivil")) {
                                        prsn.setEstadoCivil(c.getValor());
                                    }

                                    if (c.getCampo().equalsIgnoreCase("fechaExpedicion")) {
                                        try {
                                            if (c.getValor() == null || c.getValor() == "") {

                                            } else {
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                prsn.setFechaExpedicion(sdf.parse(c.getValor()));
                                            }
                                        } catch (Exception e) {
                                            System.out.println(" fechaExpedicion --> " + e.getMessage());
                                        }
                                    }
                                    if (c.getCampo().equalsIgnoreCase("fechaNacimiento")) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                            prsn.setFechaNacimiento(sdf.parse(c.getValor()));
                                        } catch (Exception e) {
                                            System.out.println(" fechaNacimiento --> " + e.getMessage());
                                        }
                                    }

                                    if (c.getCampo().equalsIgnoreCase("individualDactilar") || c.getCampo().equalsIgnoreCase("codigo")) {
                                        prsn.setCodigo(c.getValor());
                                    }
                                    if (c.getCampo().equalsIgnoreCase("nombre")) {
                                        String[] s = c.getValor().split(" ");
                                        if (s.length == 6) {
                                            prsn.setApellido(s[0] + " " + s[1] + " " + s[2]);
                                            prsn.setNombre(s[3] + " " + s[4] + " " + s[5]);
                                        } else if (s.length == 5) {
                                            prsn.setApellido(s[0] + " " + s[1] + " " + s[2]);
                                            prsn.setNombre(s[3] + " " + s[4]);
                                        } else if (s.length == 4) {
                                            prsn.setApellido(s[0] + " " + s[1]);
                                            prsn.setNombre(s[2] + " " + s[3]);
                                        } else if (s.length == 3) {
                                            prsn.setApellido(s[0] + " " + s[1]);
                                            prsn.setNombre(s[2]);
                                        } else if (s.length == 2) {
                                            prsn.setApellido(s[0]);
                                            prsn.setNombre(s[1]);
                                        } else {
                                            prsn.setNombre(c.getValor());
                                        }
                                    }

                                }// Fin personas

                                // Generales
                                if (de.getNombre().equalsIgnoreCase("Representante Legal")) {
                                    if (c.getCampo().equalsIgnoreCase("nombre")) {
                                        prsn.setRepresentanteLegal(c.getValor());
                                    }
                                }

                                if (de.getNombre().equalsIgnoreCase("Tipo Contribuyente")) {
                                    System.out.println("    --> " + c.getCampo() + ": " + c.getValor());
                                }


                            }
                        }
                    }
                    Persona personaBD = null;
                    if (persona.getId() == null) {
                        try {
                            personaBD = repository.findTop1ByNumIdentificacionOrderByIdDesc(persona.getNumIdentificacion());
                        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException e) {
                            System.out.println("Buscando el ultimo ingresado existe mas de uno " + persona.getNumIdentificacion());
                            personaBD = repository.findTop1ByNumIdentificacionOrderByIdDesc(persona.getNumIdentificacion());
                        }
                    } else {
                        personaBD = persona;
                    }
                    System.out.println("Verificacion " + personaBD);
                    if (personaBD != null) {
                        prsn.setId(personaBD.getId());
                        prsn.setCorreo(personaBD.getCorreo());
                    } else {
                        if (persona != null && persona.getId() != null) {
                            prsn.setId(persona.getId());
                        }
                    }
                    //prsn = repository.findOne(Example.of(new Persona(prsn.getId()))).orElse(null);
                    if (persona.getNumIdentificacion().equals(prsn.getNumIdentificacion())) {
                        return prsn;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PersonaDto consultarDatosPersona(RespuestaWs data) {
        if (Utils.isNotEmptyString(data.getData())) {
            return personaMapper.toDto(consultarPersonaCartera(new Persona(data.getData())));
        } else {
            return personaMapper.toDto(consultarPersonaCartera(new Persona(data.getId())));
        }

    }

    public Persona consultarPersonaCartera(Persona persona) {
        Persona result = null;
        try {
            try {
                Persona per;
                if (persona.getNumIdentificacion() != null) {
                    if (persona.getId() != null) {
                        per = new Persona(persona.getId(), persona.getNumIdentificacion());
                    } else {
                        per = new Persona(persona.getNumIdentificacion());
                    }
                } else {
                    per = new Persona(persona.getId());
                }
                result = repository.findOne(Example.of(per)).orElse(null);
            } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException e) {
                System.out.println("Buscando el ultimo ingresado existe mas de uno " + persona.getNumIdentificacion());
                result = repository.findTop1ByNumIdentificacionOrderByIdDesc(persona.getNumIdentificacion());
            }
            if (result == null || result.getId() == null) {
                if (persona.getNumIdentificacion() != null) {
                    if (persona.getNumIdentificacion().trim().length() > 5) {
                        result = this.consultaByDinardapInter(persona);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public RespuestaWs guardarPersona(PersonaDto persona) {

        System.out.println("persona: " + persona.toString());

        try {
            Persona personaBD = personaMapper.toEntity(persona);
            personaBD = repository.save(personaBD);
            return new RespuestaWs(true, Utils.toJson(personaMapper.toDto(personaBD)), Constantes.datosCorrecto);
        } catch (Exception e) {
            return new RespuestaWs(Boolean.FALSE, null, e.getMessage());
        }
    }
}
