package org.sas.administrativo.service.talentohumano;

import com.google.gson.Gson;
import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.ServidorDatos;
import org.sas.administrativo.entity.Persona;
import org.sas.administrativo.entity.talentohumano.Servidor;
import org.sas.administrativo.entity.talentohumano.ServidorCargo;
import org.sas.administrativo.mapper.talentohumano.ServidorDatosMapper;
import org.sas.administrativo.repository.PersonaRepository;
import org.sas.administrativo.repository.talentohumano.ServidorCargoRepository;
import org.sas.administrativo.repository.talentohumano.ServidorRepository;
import org.sas.administrativo.util.Constantes;
import org.sas.administrativo.util.Utils;
import org.sas.administrativo.util.model.EstadoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;

@Service
public class ServidorService {

    @Autowired
    private ServidorRepository repository;
    @Autowired
    private ServidorCargoRepository servidorCargoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private UnidadAdministrativaService unidadAdministrativaService;
    @Autowired
    private ServidorDatosMapper servidorDatosMapper;

    public RespuestaWs guardar(Servidor data) {
        RespuestaWs respuestaWs;
        try {
            Persona p = personaRepository.save(data.getPersona());
//            data.setPersona(p);
            data = repository.save(data);

            respuestaWs = new RespuestaWs(Boolean.TRUE, Utils.toJson(data), Constantes.DATOS_GUARDADOS);
        } catch (Exception e) {
            respuestaWs = new RespuestaWs(Boolean.FALSE, null, Constantes.DATOS_NO_GUARDADOS);
        }
        return respuestaWs;
    }


    public RespuestaWs datoServidorXpersona(RespuestaWs data) {
        RespuestaWs rw = null;
        try {
            ServidorDatos servidorDatos = new ServidorDatos();
            Servidor servidor = repository.findByPersona_Id(data.getId());
            if (servidor != null) {
                ServidorCargo sc = null;
                if (servidor.getEstado().equals(EstadoType.INACTIVO.name()) || servidor.getEstado().equals(EstadoType.TEMPORAL.name())) {
                    sc = servidorCargoRepository.findTopByServidor_IdOrderByIdDesc(servidor.getId());
                } else {
                    sc = servidorCargoRepository.findByServidor_IdAndEstado(servidor.getId(), EstadoType.ACTIVO.name());
                }

                if (sc != null && sc.getId() != null) {
                    servidorDatos.setId(servidor.getId());
                    servidorDatos.setServidor(servidor.getPersona().getNombre() + " " + servidor.getPersona().getApellido());
                    servidorDatos.setIdentificacion(servidor.getPersona().getNumIdentificacion());
                    servidorDatos.setPersonaID(servidor.getPersona().getId());
                    servidorDatos.setNombres(servidor.getPersona().getNombre());
                    servidorDatos.setApellidos(servidor.getPersona().getApellido());

                    servidorDatos.setCargo(sc.getCargo().getPerfilCargo().getNombre());


                    if (sc.getCargo().getUnidad() != null) {
                        servidorDatos.setUnidadAdministrativa(sc.getCargo().getUnidad().getNombre());
                        servidorDatos.setUnidadAdministrativaID(sc.getCargo().getUnidad().getId());
                    }

                    // buscamos a que direccion pertenece la unidad del servidor
                    if (servidorDatos.getUnidadAdministrativaID() != null) {
                        servidorDatos.setDireccionAdministrativa(unidadAdministrativaService.obtenerDireccionByIdUnidad(servidorDatos.getUnidadAdministrativaID()));
                    }
                    servidorDatos.setTitulo(servidor.getPersona().getTituloProfesional());
                    servidorDatos.setCorreoPersonal(servidor.getPersona().getCorreo());
                    servidorDatos.setCorreoInstitucional(servidor.getEmailInstitucion());
                    servidorDatos.setEmailInstitucion(servidor.getEmailInstitucion());
                    servidorDatos.setTelefonoInstitucional(servidor.getTelefonoInstitucional());
                    servidorDatos.setExtensionTelefonica(servidor.getExtensionTelefonica());
                    if (servidor.getPersona().getFechaNacimiento() != null) {
                        int mayorEdad = Utils.calcularAnios(servidor.getPersona().getFechaNacimiento());
                        if (mayorEdad >= 18) {
                            String hoy = Utils.dateFormatPattern("dd-MM", new Date());
                            servidorDatos.setFechaCumpleanio(Utils.dateFormatPattern("dd-MM-yyyy", servidor.getPersona().getFechaNacimiento()));
                            String today = servidorDatos.getFechaCumpleanio().substring(0, 5);
                            if (today.equals(hoy)) {
                                servidorDatos.setCumpleanio(Boolean.TRUE);
                            } else {
                                servidorDatos.setCumpleanio(Boolean.FALSE);
                            }
                        } else {
                            servidorDatos.setCumpleanio(Boolean.FALSE);
                        }
                    } else {
                        servidorDatos.setCumpleanio(Boolean.FALSE);
                    }
                    rw = new RespuestaWs(Boolean.TRUE, new Gson().toJson(servidorDatos), Constantes.EXISTE_REGISTRO);
                } else {
                    rw = servidorXpersona(servidorDatos, data.getId(), servidor);
                }
            } else { //SE AGREGRA PARA QUE POR LO MENOS DEVUELVA LOS DATOS DE LA PERSONA
                rw = servidorXpersona(servidorDatos, data.getId(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            rw = new RespuestaWs(Boolean.FALSE, "", Constantes.NO_DATO_ERROR_MENSAJE);
        }
        return rw;
    }

    public RespuestaWs servidorXpersona(ServidorDatos servidorDatos, Long id, Servidor servidor) {
        Persona persona = personaRepository.findByIdPersona(id);
        if (persona != null) {
            servidorDatos.setServidor(persona.getNombre() + " " + persona.getApellido());
            servidorDatos.setIdentificacion(persona.getNumIdentificacion());
            servidorDatos.setPersonaID(persona.getId());
            servidorDatos.setNombres(persona.getNombre());
            servidorDatos.setApellidos(persona.getApellido());
            servidorDatos.setTitulo(persona.getTituloProfesional());
            servidorDatos.setCargo("NO EXISTE UN CARGO ASOCIADO");
            if (servidor != null) {
                servidorDatos.setId(servidor.getId());
                servidorDatos.setCorreoInstitucional(servidor.getEmailInstitucion());
                servidorDatos.setEmailInstitucion(servidor.getEmailInstitucion());
                servidorDatos.setTelefonoInstitucional(servidor.getTelefonoInstitucional());
                servidorDatos.setExtensionTelefonica(servidor.getExtensionTelefonica());
            }
            if (persona.getFechaNacimiento() != null) {
                int mayorEdad = Utils.calcularAnios(persona.getFechaNacimiento());
                if (mayorEdad >= 18) {
                    String hoy = Utils.dateFormatPattern("dd-MM", new Date());
                    servidorDatos.setFechaCumpleanio(Utils.dateFormatPattern("dd-MM-yyyy", persona.getFechaNacimiento()));
                    String today = servidorDatos.getFechaCumpleanio().substring(0, 5);
                    if (today.equals(hoy)) {
                        servidorDatos.setCumpleanio(Boolean.TRUE);
                    } else {
                        servidorDatos.setCumpleanio(Boolean.FALSE);
                    }
                } else {
                    servidorDatos.setCumpleanio(Boolean.FALSE);
                }

            }
            return new RespuestaWs(Boolean.TRUE, new Gson().toJson(servidorDatos), Constantes.EXISTE_REGISTRO);
        }
        return null;
    }

    public ServidorDatos findServidorDatos(Servidor data) {
        ServidorDatos result = null;
        try {
            Servidor s = this.findServidor(data);//repository.findOne(Example.of(data)).orElse(null);
            result = servidorDatosMapper.toDto(s);
            if (s != null) {
                ServidorCargo sc = servidorCargoRepository.findByServidor_IdAndEstadoIn(s.getId(), Arrays.asList(EstadoType.ACTIVO.name(), EstadoType.TEMPORAL.name()));
                if (sc != null && sc.getId() != null) {
                    result.setCargo(sc.getCargo().getPerfilCargo().getNombre());
                    if (sc.getCargo().getUnidad() != null) {
                        result.setUnidadAdministrativa(sc.getCargo().getUnidad().getNombre());
                        result.setUnidadAdministrativaID(sc.getCargo().getUnidad().getId());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar ServidorDatos: " + data);
            e.printStackTrace();
        }
        return result;
    }

    public Servidor findServidor(Servidor data) {
        try {
            Optional<Servidor> op = null;
            if (data.getId() != null) {
                op = repository.findById(data.getId());
            } else if (data.getPersona() != null && data.getPersona().getNumIdentificacion() != null) {
                op = repository.getServidorByPersona_NumIdentificacion(data.getPersona().getNumIdentificacion());
            } else {
                op = Optional.ofNullable(repository.findOne(Example.of(data)).orElse(null));
            }
            return op.orElse(null);
        } catch (Exception e) {
//            LOG.log(Level.SEVERE, "findServidor>>>", e);
            return null;
        }
    }

    public ServidorDatos getServidorByUser(String user) {
        ServidorDatos s = null;
        Long idServidor = null;
        try {
            idServidor = repository.getIdServidroByUsuario(user);
            if (idServidor != null) {
                Servidor sd = repository.getById(idServidor);
                s = servidorDatosMapper.toDto(sd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

}
