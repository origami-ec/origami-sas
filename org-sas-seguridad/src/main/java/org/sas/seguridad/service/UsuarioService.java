package org.sas.seguridad.service;

import org.sas.seguridad.conf.AppProps;
//import org.ibarra.seguridad.dto.*;
import org.sas.seguridad.dto.*;
import org.sas.seguridad.entity.PersonaFD;
import org.sas.seguridad.entity.Usuario;
import org.sas.seguridad.entity.UsuarioVerificacion;
import org.sas.seguridad.mapper.UsuarioVerificacionMapper;
import org.sas.seguridad.repository.UserRepo;
import org.sas.seguridad.util.Constantes;
import org.sas.seguridad.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsuarioService {

    private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UsuarioClaveService usuarioClaveService;
    @Autowired
    private UsuarioVerificacionService usuarioVerificacionService;
    @Autowired
    private UsuarioVerificacionMapper usuarioVerificacionMapper;
    @Autowired
    private AppProps appProps;
    @Autowired
    private RemoteService remoteService;
    @Autowired
    private EncryptDecrypt encryptDecrypt;
    @Autowired
    private AdministrativoService administrativoService;
    @Autowired
    private MotivacionesService service;
    @Autowired
    private UsuarioRolService rolService;


    public Usuario findByUsuario(String usuario) {
        return userRepo.findByUsuario(usuario);
    }

    public UsuarioInicioSesion iniciarSesion(UsuarioInicioSesion usuario) {
        Usuario u = userRepo.findByUsuario(usuario.getUsuario());
        List<String> rolesUsuario = rolService.rolesXUsuario(usuario.getUsuario());
        usuario.setId(u.getId());
        usuario.setDobleVerificacion(u.getDobleVerificacion());
        usuario.setActiveDirectory(u.getActiveDirectory());
        usuario.setEstado(Constantes.ok);
        usuario.setPersona(u.getPersonaId());
        try {
            usuario.setFrase(service.consultarFraseMotivacion().getDescripcion());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        usuario.setRoles(rolesUsuario);
        usuario.setNotificarCorreo(u.getNotificarCorreo());
        try {
            ServidorDatos sd = administrativoService.findServidorDatos(new RespuestaWs(u.getPersonaId()));
            usuario.setServidor(sd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return usuario;
    }

    @Cacheable(value = "usuarioPorPersona", key = "#data.id", unless = "#result == null")
    public Usuario usuarioXpersona(RespuestaWs data) {
        try {
            return userRepo.findByPersona(data.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UsuarioDetalle personaXusuario(RespuestaWs data) {
        Usuario user = userRepo.findByUsuario(data.getData());
        UsuarioDetalle usuario = new UsuarioDetalle();
        usuario.setId(user.getId());
        usuario.setUsuario(user.getUsuario());
        usuario.setEstado(user.getEstado());
        ServidorDatos sd = administrativoService.findServidorDatos(new RespuestaWs(user.getPersonaId()));
        usuario.setServidor(sd);
        return usuario;
    }

    public UsuarioVerificacionDTO generarCodigoVerificacion(Long idUsuario) {
        Usuario usuario = userRepo.getById(idUsuario);
        List<UsuarioVerificacion> uDB = usuarioVerificacionService.findAll(new UsuarioVerificacion(new Usuario(idUsuario), Boolean.TRUE));
        if (!uDB.isEmpty()) {
            for (UsuarioVerificacion uv : uDB) {
                uv.setEstado(Boolean.FALSE);
                usuarioVerificacionService.save(uv);
            }
        }
        UsuarioVerificacionDTO uvDto = usuarioVerificacionMapper.toDTO(usuarioVerificacionService.save(new UsuarioVerificacion(userRepo.findOne(Example.of(new Usuario(idUsuario))).orElse(null), Long.valueOf(Utils.codigoVerificacion(appProps.getCodigoLongitud())), new Date(), Boolean.TRUE)));

        PersonaFD persona = administrativoService.find(new PersonaFD(usuario.getPersonaId()));

        if (persona != null && persona.getCorreo() != null) {
            uvDto.getUsuario().setCorreoEncriptado(persona.getCorreo().replaceAll("(?<=.{3}).(?=.*@)", "*"));
            uvDto.getUsuario().setCorreo(persona.getCorreo());
        }

        return uvDto;
    }

    public UsuarioVerificacionDTO verificarCodigo(UsuarioVerificacionDTO data) {

        if (data.getCodigoVerificacion().toString().length() != appProps.getCodigoLongitud()) {
            return new UsuarioVerificacionDTO(data.getId(), Constantes.CODIGO_TAMANIO_ERROR, Constantes.TAMANIO_ERROR_MENSAJE);
        }
        UsuarioVerificacion uv = usuarioVerificacionService.find(usuarioVerificacionMapper.toEntity(data));
        if (uv != null) {
            if (uv.getFecha().after(new Date())) {
                return new UsuarioVerificacionDTO(data.getId(), Constantes.CODIGO_TIEMPO_ERROR, Constantes.TIEMPO_ERROR_MENSAJE);
            }
            if (!Utils.getHour(new Date()).equals(Utils.getHour(uv.getFecha()))) {
                return new UsuarioVerificacionDTO(data.getId(), Constantes.CODIGO_TIEMPO_ERROR, Constantes.TIEMPO_ERROR_MENSAJE);
            }
            if ((Utils.getMinute(uv.getFecha()) - Utils.getMinute(new Date())) > appProps.getCodigoTiempo()) {
                return new UsuarioVerificacionDTO(data.getId(), Constantes.CODIGO_TIEMPO_ERROR, Constantes.TIEMPO_ERROR_MENSAJE);
            }
            uv.setEstado(Boolean.FALSE);
            UsuarioVerificacionDTO uvDto = usuarioVerificacionMapper.toDTO(usuarioVerificacionService.save(uv));
            uvDto.setCodigoError(Constantes.CODIGO_OK);
            uvDto.setMensaje(Constantes.OK_MENSAJE);
            return uvDto;
        }
        return new UsuarioVerificacionDTO(data.getId(), Constantes.CODIGO_NO_DATO_ERROR, Constantes.NO_DATO_ERROR_MENSAJE);
    }

    public UsuarioVerificacionDTO save(UsuarioVerificacionDTO data) {
        return usuarioVerificacionMapper.toDTO(usuarioVerificacionService.save(usuarioVerificacionMapper.toEntity(data)));
    }

    public Usuario guardar(Usuario usuario) {
        usuario.setFechaCaducidad(new Date());
        usuario.setEstado(appProps.getUsuarioPendiente());
        //usuario.setActiveDirectory(encryptDecrypt.encriptarTexto(usuario.getActiveDirectory()));
        Usuario usuarioBD = userRepo.save(usuario);
        enviarCorreoVerificacion(usuarioBD);
        return usuarioBD;
    }

    public Usuario editar(Usuario usuario) {
        try {
            userRepo.save(usuario);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public void enviarCorreoVerificacion(Usuario usuario) {
        PersonaFD persona = administrativoService.find(new PersonaFD(usuario.getPersonaId()));

        if (persona != null) {
            ServidorDatos sd = administrativoService.findServidorDatos(new RespuestaWs(persona.getId()));
            System.out.println("appProps.getActivarUsuarioMensaje(): " + appProps.getActivarUsuarioMensaje());
            String nombres = persona.getApellido() + " " + persona.getNombre();
            Correo correo = new Correo();
            correo.setAsunto(appProps.getActivarUsuarioAsunto());
            if (Utils.isNotEmptyString(persona.getCorreo()) && Utils.isNotEmptyString(sd.getCorreoInstitucional())) {
                if (!persona.getCorreo().equals(sd.getCorreoInstitucional())) {
                    correo.setDestinatario(persona.getCorreo() + ";" + sd.getCorreoInstitucional());
                } else {
                    correo.setDestinatario(sd.getCorreoInstitucional());
                }

            } else if (Utils.isNotEmptyString(sd.getCorreoInstitucional())) {
                correo.setDestinatario(sd.getCorreoInstitucional());
            } else if (Utils.isNotEmptyString(persona.getCorreo())) {
                correo.setDestinatario(persona.getCorreo());
            }
            if (Utils.isNotEmptyString(correo.getDestinatario())) {
                correo.setTexto(String.format(appProps.getActivarUsuarioMensaje(), nombres));
                System.out.println(correo.getTexto());
                correo.setVinculo(appProps.getUrlApp() + "/actualizarClave?usuario=" + usuario.getUsuario() + "&codigo=" + encryptDecrypt.encriptarTexto(appProps.getCodigoSha() + usuario.getUsuario()) + "&referencia=" + new Date().getTime());
                correo.setTextoVinculo(appProps.getActivarUsuarioVinculo());
                remoteService.enviarCorreo(correo);
            }
        }
    }

    public Usuario correoCambioClave(Usuario usuario) {
        try {
            PersonaFD persona = administrativoService.find(new PersonaFD(usuario.getPersonaId()));
            System.out.println("//////// CorreoCambioClave persona id: " + usuario.getPersonaId() + " persona " + persona);
            if (persona != null) {
                ServidorDatos servidorDatos = administrativoService.findServidorDatos(new RespuestaWs(persona.getId()));
//                System.out.println("Datos del servidor: " + servidorDatos);
                //PRIMERO SE ACTUALIZA OTRAVEZ A ESTADO PENDIENTE
                usuario.setEstado(appProps.getUsuarioPendiente());
                usuario.setPersona(null);
                userRepo.save(usuario);
                //ENVIO DE NOTIFACION POR CORREO PARA LA CONTRASENIA
                Correo correo = new Correo();
                correo.setAsunto(appProps.getActualizarUsuarioAsunto());
                if (servidorDatos != null) {
                    correo.setDestinatario(servidorDatos.getCorreoInstitucional());
                } else {
                    correo.setDestinatario(persona.getCorreo());
                }
                correo.setTexto(String.format(appProps.getActualizarUsuarioMensaje(), persona.getApellido() + persona.getNombre()));
                correo.setVinculo(appProps.getUrlApp() + "/actualizarClave?usuario=" + usuario.getUsuario() + "&codigo=" + encryptDecrypt.encriptarTexto(appProps.getCodigoSha() + usuario.getUsuario()) + "&referencia=" + new Date().getTime());
                correo.setTextoVinculo(appProps.getActualizarUsuarioVinculo());
                try {
                    remoteService.enviarCorreo(correo);
                } catch (Exception x) {
                    Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, "", x);
                }
            }
            return usuario;
        } catch (Exception e) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, "", e);
        }
        return null;
    }

    public Usuario find(Usuario usuario) {
        return userRepo.findOne(Example.of(usuario)).orElse(null);
    }

    public String find(Long code) {
        return userRepo.getById(code) != null ? userRepo.getById(code).getUsuario() : null;
    }

    public void delete(Usuario usuario) {
        userRepo.delete(usuario);
    }

    public List<Usuario> findAll(Usuario usuario) {
        return userRepo.findAll(Example.of(usuario));
    }

    public Usuario findByPersona(Long usuario) {
        return userRepo.findByPersona(usuario);
    }

    public Map<String, List> findAll(Usuario usuario, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
        Page<Usuario> result = userRepo.findAll(Example.of(usuario, exampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(userRepo.count(Example.of(usuario, exampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public UsuarioInicioSesion updatePassword(UsuarioInicioSesion data) {
        Usuario u = find(new Usuario(data.getId()));
        if (u.getEstado().equals(appProps.getUsuarioActivo())) {
            return null;
        }
        usuarioClaveService.updatePassword(data);
        u.setEstado(appProps.getUsuarioActivo());
        u.setFechaCaducidad(new Date());
        userRepo.save(u);
        return data;
    }

    public UsuarioInicioSesion updatePassword1(UsuarioInicioSesion data) {
        try {
            Usuario u = find(new Usuario(data.getId()));
//        if (u.getEstado().equals(appProps.getUsuarioActivo())) {
//            return null;
//        }
            usuarioClaveService.updatePassword(data);
            u.setEstado(appProps.getUsuarioActivo());
            u.setFechaCaducidad(new Date());
            userRepo.save(u);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida el usuario por numero de udentificacion
     *
     * @param data
     * @return
     */
    public Data validarUsuarioPersona(Data data) {
        PersonaFD persona = administrativoService.find(new PersonaFD(data.getData()));
        if (persona != null) {
            Usuario u = userRepo.findByPersona(persona.getId());
            if (u != null) {
                return new Data(1L, String.format(Constantes.EXISTE_USUARIO, data.getData(), u.getEstado()));
            }
        }
        return new Data(0L, Constantes.ok);
    }

    /**
     * Busca el usuario por numero de udentificacion
     *
     * @param data
     * @return
     */
    public UsuarioInicioSesion buscarUsuarioIdentificacion(Persona data) {
        PersonaFD persona = administrativoService.find(new PersonaFD(data.getNumIdentificacion()));
        if (persona != null) {
            Usuario u = userRepo.findByPersona(persona.getId());
            if (u != null) {
                UsuarioInicioSesion usuario = new UsuarioInicioSesion();
                List<String> rolesUsuario = rolService.rolesXUsuario(usuario.getUsuario());
                usuario.setId(u.getId());
                usuario.setDobleVerificacion(u.getDobleVerificacion());
                usuario.setActiveDirectory(u.getActiveDirectory());
                usuario.setEstado(Constantes.ok);
                usuario.setPersona(u.getPersonaId());
                usuario.setFrase(service.consultarFraseMotivacion().getDescripcion());
                usuario.setRoles(rolesUsuario);
                usuario.setNotificarCorreo(u.getNotificarCorreo());
                try {
                    ServidorDatos sd = administrativoService.findServidorDatos(new RespuestaWs(u.getPersonaId()));
                    usuario.setServidor(sd);
                } catch (Exception e) {
                    System.out.println("Error al consuktar datos del servidor: " + e.getMessage());
                }
                return usuario;
            }
        }
        return null;
    }

    public Usuario findByPersonaIdAndEstado(Long personaId) {
        try {
            return userRepo.findByPersonaIdAndEstado(personaId, "ACTIVO");
        } catch (Exception e) {
            return null;
        }
    }

    public Usuario updateUser(Usuario us) {
        try {
            us = userRepo.save(us);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return us;
    }
}
