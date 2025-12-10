package org.sas.administrativo.service.talentohumano;

import com.google.gson.Gson;
import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.UnidadAdministrativaDto;
import org.sas.administrativo.entity.talentohumano.Servidor;
import org.sas.administrativo.entity.talentohumano.UnidadAdministrativa;
import org.sas.administrativo.mapper.talentohumano.UnidadAdministrativaMapper;
import org.sas.administrativo.repository.talentohumano.ServidorCargoRepository;
import org.sas.administrativo.repository.talentohumano.ServidorRepository;
import org.sas.administrativo.repository.talentohumano.UnidadAdministrativaRepository;
import org.sas.administrativo.util.Constantes;
import org.sas.administrativo.util.Utils;
import org.sas.administrativo.util.ValoresCodigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UnidadAdministrativaService {

    private static final Logger LOG = Logger.getLogger(UnidadAdministrativaService.class.getName());

    @Autowired
    private UnidadAdministrativaRepository repository;
    @Autowired
    private UnidadAdministrativaMapper unidadAdministrativaMapper;
    @Autowired
    private ServidorCargoRepository servidorCargoRepository;
  //  @Autowired
 //   private ServidorDatosMapper servidorDatosMapper;
    @Autowired
    private ServidorRepository servidorRepository;

    public UnidadAdministrativaDto obtenerDireccionByIdUnidad(Long idunidad) {
        Servidor responsable = null;
        UnidadAdministrativa direccion = repository.getById(idunidad);
        try {
            /**if (direccion.getCodigo() != null && direccion.getCodigo().equals("ALC")) {
             UnidadAdministrativaDto dto = unidadAdministrativaMapper.toDto(direccion);
             if (direccion.getDirector() != null) {
             dto.setDirector(servidorDatosMapper.toDto(direccion.getDirector()));
             }
             }
            if (direccion.getResponsable() != null) {
                responsable = direccion.getResponsable();
            }*/
            while (direccion != null) {
                if (direccion.getTipo() != null &&
                        (direccion.getTipo().getCodigo().equals("DIR") ||
                                direccion.getCodigo().equals("ALC"))) {
                    UnidadAdministrativaDto dto = unidadAdministrativaMapper.toDto(direccion);

                    return dto;
                }
                direccion = direccion.getPadre();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener dirección por ID unidad", e);
        }
        return null;
    }

    public List<UnidadAdministrativa> findAll() {
        UnidadAdministrativa u = new UnidadAdministrativa();
        u.setEstado("ACTIVO");
        return repository.findAll(Example.of(u));
    }


    public List<UnidadAdministrativaDto> consultarDepartamentos() {
        UnidadAdministrativa u = new UnidadAdministrativa();
        u.setEstado("ACTIVO");
        List<UnidadAdministrativa> list = repository.findAll(Example.of(u));
        List<UnidadAdministrativaDto> dtos = unidadAdministrativaMapper.toDto(list);
        return dtos;
    }

    public List<UnidadAdministrativaDto> getAllPadre(String estado) {
        try {
            List<UnidadAdministrativa> list = repository.findUnidadAdministrativaByPadreIsNullAndEstado(estado);
            return unidadAdministrativaMapper.toDto(list);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    //@Cacheable(value = "unidadesDatos", key = "#data.padre.id")
    public List<UnidadAdministrativaDto> getUnidadesDatos(UnidadAdministrativa data) {
        if (data == null || data.getPadre() == null) {
            return Collections.emptyList();
        }
        List<UnidadAdministrativa> list = repository.findAllByPadre_IdAndEstado(data.getPadre().getId(), "ACTIVO");
        return unidadAdministrativaMapper.toDto(list);
    }

    public List<UnidadAdministrativa> getUnidadAdministrativa(UnidadAdministrativa data) {
        try {
            if (data != null) {
                if (data.getPadre() != null) {
                    return repository.findAllByPadre_IdAndEstado(data.getPadre().getId(), "ACTIVO");
                } else {
                    return repository.findAllByPadre_IdAndEstado(data.getId(), "ACTIVO");
                }
            } else {
                return null;
            }
//            return repository.findAll(Example.of(data));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }


    public UnidadAdministrativa consultarUnidadAdministrativa(UnidadAdministrativa data) {
        try {
            Optional<UnidadAdministrativa> unidadAdministrativa = repository.findOne(Example.of(data));
            UnidadAdministrativa response = new UnidadAdministrativa();

            if (unidadAdministrativa.isPresent()) {
                response.setId(unidadAdministrativa.get().getId());
                response.setNombre(unidadAdministrativa.get().getNombre());
                response.setCodigo(unidadAdministrativa.get().getCodigo());
                //response.setResponsable(unidadAdministrativa.get().getResponsable());
            }

            return response;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public RespuestaWs guardar(UnidadAdministrativa data) {
        try {
            boolean edit = data.getId() != null;
            if (edit && data.getEstado().equalsIgnoreCase(ValoresCodigo.estadoInactivo)) {

            }
            /*if (data.getDirector() == null || data.getDirector().getId() == null) {
                data.setDirector(null);
            }*/
            data = repository.save(data);
            return new RespuestaWs(Boolean.TRUE, (edit ? Utils.toJson(data) : new Gson().toJson(data)), "Datos fueron " + (edit ? "Editados" : "Creados" + " con exito.."));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new RespuestaWs(Boolean.FALSE, "", Constantes.DATOS_NO_GUARDADOS);
        }
    }

    public List<UnidadAdministrativaDto> findAllUnidadDto() {
        UnidadAdministrativa u = new UnidadAdministrativa();
        u.setEstado("ACTIVO");
        return unidadAdministrativaMapper.toDto(repository.findAll(Example.of(u)));
    }


    public List<UnidadAdministrativa> findAllUnidades() {
        UnidadAdministrativa u = new UnidadAdministrativa();
        u.setEstado("ACTIVO");
        return repository.findAll(Example.of(u));
    }

    public UnidadAdministrativa getUnidadAdministrativaById(Long id) {
        try {
            Optional<UnidadAdministrativa> unidadAdministrativa = repository.findById(id);
            UnidadAdministrativa response = new UnidadAdministrativa();

            if (unidadAdministrativa.isPresent()) {
                response.setId(unidadAdministrativa.get().getId());
                response.setNombre(unidadAdministrativa.get().getNombre());
                response.setCodigo(unidadAdministrativa.get().getCodigo());
                response.setTipo(unidadAdministrativa.get().getTipo());
            }

            return response;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public List<UnidadAdministrativaDto> findAllDirecciones() {
        return unidadAdministrativaMapper.toDto(repository.findAllByEstadoAndTipo_Codigo("ACTIVO", "DIR"));
    }

    public List<UnidadAdministrativaDto> fillAllDireccionesCompletas() {
        return unidadAdministrativaMapper.toDto(repository.findAllByEstadoAndTipo_CodigoIn("ACTIVO", List.of("DIR", "GEN", "GENS", "EMP", "SUB", "EADS")));
    }

    public List<UnidadAdministrativa> getUnidadesByPadre(UnidadAdministrativa data) {
        List<UnidadAdministrativa> unidadAdministrativas = new ArrayList<>();
        try {
            if (data != null) {
                List<UnidadAdministrativa> unidades = repository.findAllByPadre_IdAndEstado(data.getId(), "ACTIVO");
                if (Utils.isNotEmpty(unidades)) {
                    unidadAdministrativas.addAll(unidades);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return unidadAdministrativas;
    }



    public List<UnidadAdministrativa> buscarUnidadesHijas(Long idunidad) {
        List<UnidadAdministrativa> unidades = new ArrayList<>();
        List<UnidadAdministrativa> unidadesDirectas = repository.findAllByPadre_IdAndEstado(idunidad, "ACTIVO");

        for (UnidadAdministrativa u : unidadesDirectas) {
            unidades.add(u);
            List<UnidadAdministrativa> unidadesHijas = buscarUnidadesHijas(u.getId());
            if (Utils.isNotEmpty(unidadesHijas)) {
                unidades.addAll(unidadesHijas);
            }
        }

        return unidades;
    }


    /*public RespuestaWs getServidoresDireccionesUnidades(Long idUnidad) {
        try {
            UnidadAdministrativa unidadAdm = repository.findById(idUnidad).orElse(null);

            if (unidadAdm == null) {
                LOG.log(Level.WARNING, "Unidad administrativa no encontrada con id: {0}", idUnidad);
                return new RespuestaWs(Boolean.FALSE, null, "Unidad administrativa no encontrada");
            }

            List<ServidorDatos> servidoresDatos = new ArrayList<>();

            if (unidadAdm.getNombre() != null && unidadAdm.getNombre().toUpperCase().contains("DIREC")) {
                LOG.log(Level.INFO, "Es dirección, se procederá a buscar los servidores de la dirección y sus hijos");

                List<ServidorCargo> servidoresDireccion = servidorCargoRepository.findAllByCargo_Unidad_IdAndEstado(idUnidad, "ACTIVO");
                if (servidoresDireccion != null && !servidoresDireccion.isEmpty()) {
                    for (ServidorCargo servidor : servidoresDireccion) {
                        ServidorDatos sd = new ServidorDatos();
                        sd.setIdentificacion(servidor.getServidor().getPersona().getNumIdentificacion());
                        sd.setNombres(servidor.getServidor().getPersona().getNombre());
                        sd.setApellidos(servidor.getServidor().getPersona().getApellido());
                        sd.setCargo(servidor.getCargo().getPerfilCargo().getNombre());
                        sd.setDireccion(servidor.getCargo().getUnidad().getNombre());
                        servidoresDatos.add(sd);
                    }
                }
                List<UnidadAdministrativa> hijos = repository.findHijosByUnidad(idUnidad);
                if (hijos != null && !hijos.isEmpty()) {
                    for (UnidadAdministrativa hijo : hijos) {
                        LOG.log(Level.INFO, "Hijo encontrado: {0}", hijo.getNombre());
                        List<ServidorCargo> servidores = servidorCargoRepository.findAllByCargo_Unidad_IdAndEstado(hijo.getId(), "ACTIVO");
                        if (servidores != null && !servidores.isEmpty()) {
                            for (ServidorCargo servidor : servidores) {
                                ServidorDatos sd = new ServidorDatos();
                                sd.setIdentificacion(servidor.getServidor().getPersona().getNumIdentificacion());
                                sd.setNombres(servidor.getServidor().getPersona().getNombre());
                                sd.setApellidos(servidor.getServidor().getPersona().getApellido());
                                sd.setCargo(servidor.getCargo().getPerfilCargo().getNombre());
                                sd.setDireccion(servidor.getCargo().getUnidad().getNombre());
                                servidoresDatos.add(sd);
                            }
                        }
                    }
                } else {
                    LOG.log(Level.INFO, "No se encontraron hijos para la unidad con id: {0}", idUnidad);
                }
            } else {
                LOG.log(Level.INFO, "No es dirección, se procederá a buscar padres e hijos");
                List<UnidadAdministrativa> padresEHijos = repository.findPadresEHijosByUnidad(idUnidad);
                if (padresEHijos != null && !padresEHijos.isEmpty()) {
                    for (UnidadAdministrativa unidad : padresEHijos) {
                        LOG.log(Level.INFO, "Unidad encontrada: {0}", unidad.getNombre());
                        List<ServidorCargo> servidores = servidorCargoRepository.findAllByCargo_Unidad_IdAndEstado(unidad.getId(), "ACTIVO");
                        if (servidores != null && !servidores.isEmpty()) {
                            for (ServidorCargo servidor : servidores) {
                                ServidorDatos sd = new ServidorDatos();
                                sd.setIdentificacion(servidor.getServidor().getPersona().getNumIdentificacion());
                                sd.setNombres(servidor.getServidor().getPersona().getNombre());
                                sd.setApellidos(servidor.getServidor().getPersona().getApellido());
                                sd.setCargo(servidor.getCargo().getPerfilCargo().getNombre());
                                sd.setDireccion(servidor.getCargo().getUnidad().getNombre());
                                servidoresDatos.add(sd);
                            }
                        }
                    }
                } else {
                    LOG.log(Level.INFO, "No se encontraron padres ni hijos para la unidad con id: {0}", idUnidad);
                }
            }

            return new RespuestaWs(Boolean.TRUE, servidoresDatos, "Servidores obtenidos correctamente");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener servidores", e);
            return new RespuestaWs(Boolean.FALSE, null, "Error interno del servidor");
        }
    }*/





    public List<UnidadAdministrativaDto> listarUnidadesAdministrativasPorPadre(List<UnidadAdministrativa> data) {
        return unidadAdministrativaMapper.toDto(repository.listarUnidadesPorUnaListaDePadres(data));
    }

    public String buscarUnidadAdministrativa(Long idservidor) {
        String unidad = repository.buscarUnidadAdmistrativaServidor(idservidor);
        if (unidad != null) {
            return unidad.trim().toUpperCase();
        }
        return null;
    }


}
