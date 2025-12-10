package org.sas.zull.security;


import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import org.sas.zull.conf.AppProps;
import org.sas.zull.dto.ActiveDirectoryModel;
import org.sas.zull.dto.UsuarioInicioSesion;
import org.sas.zull.entity.Usuario;
import org.sas.zull.entity.UsuarioAcceso;
import org.sas.zull.service.*;
import org.sas.zull.util.Constantes;
import org.sas.zull.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService service;
    @Autowired
    private UsuarioAcessoService usuarioAcessoService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RestService restService;
    @Autowired
    private AppProps appProps;
    @Autowired
    private ActiveDirectory activeDirectory;
    private Logger logger = Logger.getLogger(JwtUserDetailsService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = service.findUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException(String.format("Usuario no encontrado", username));
        } else {
            return new User(usuario.getUsuario(), usuario.getClave(),
                    new ArrayList<>());
        }
    }

    public JwtResponse loadUserByUsername(String username, String clave, MultiValueMap<String, String> headers) throws UsernameNotFoundException {
        try {
            String sha256 = Hashing.sha256()
                    .hashString(clave, StandardCharsets.UTF_8)
                    .toString();
            Usuario usuario = service.find(new Usuario(username));

            if (usuario == null) {
                return new JwtResponse(null, Constantes.usuarioNoEncontrado, Boolean.FALSE, null);
                //throw new UsernameNotFoundException(String.format("Usuario no encontrado", username));
            } else {
                if (usuario.getEstado().equals(appProps.getUsuarioBloqueado())) {
                    return new JwtResponse(null, Constantes.usuarioBloqueado, Boolean.FALSE, null);
                }
                if (usuario.getEstado().equals(appProps.getUsuarioPendiente())) {
                    return new JwtResponse(null, Constantes.usuarioBloqueado, Boolean.FALSE, null);
                } else {
                    if (!usuario.getClave().equals(sha256)) {//INTENTO FALLIDO
                        UsuarioAcceso usuarioAcceso = usuarioAcessoService.guardarAcceso(usuario.getId(), headers.getFirst("ip"), headers.getFirst("os"), headers.getFirst("mac"), Constantes.FALLIDO);
                        if (usuarioAcceso.getIntento().equals(Constantes.INTENTOS_SESION_DEFAULT)) {
                            return new JwtResponse(null, Constantes.usuarioBloqueado, Boolean.FALSE, null);
                        } else {
                            return new JwtResponse(null, String.format(Constantes.usuarioFallido, usuarioAcceso.getIntento() + " de " + Constantes.INTENTOS_SESION_DEFAULT, Constantes.INTENTOS_SESION_DEFAULT), Boolean.FALSE, null);
                        }
                    } else {
                        return inicioSesionWS(usuario.getId(), username, clave, headers, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JwtResponse(null, String.format(Constantes.usuarioNoEncontrado, username), Boolean.FALSE, null);
        }
    }

    public JwtResponse autenticarADTodos(JwtRequest authenticationRequest, MultiValueMap<String, String> headers) {
        try {
            ActiveDirectoryModel exitoso = activeDirectory.authenticateJndi2(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            if (!exitoso.getEstado()) {
                return new JwtResponse(null, Constantes.usuarioADCredencialError, Boolean.FALSE, null);
            } else {
                return inicioSesionWS(100L, authenticationRequest.getUsername(), authenticationRequest.getPassword(), headers, exitoso);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JwtResponse(null, String.format(Constantes.usuarioADNoEncontrado, authenticationRequest.getUsername()), Boolean.FALSE, null);
        }

    }

    public JwtResponse autenticarAD(JwtRequest authenticationRequest, MultiValueMap<String, String> headers) {
        try {
            String usr = authenticationRequest.getUsername();
            /*if(usr.contains("@ibarra.gob.ec")){
                usr = usr.replace(  "@ibarra.gob.ec", "");
            }
            logger.info("usr: " + usr);*/
            Usuario usuario = service.findXactiveDirectory(usr);
            if (usuario == null) {
                return new JwtResponse(null, Constantes.usuarioNoEncontrado, Boolean.FALSE, null);
                //throw new UsernameNotFoundException(String.format("No existe ningún usuario asociado con el Active directory de:", authenticationRequest.getUsername()));
            }
            if (usuario.getEstado().equals(appProps.getUsuarioBloqueado())) {
                return new JwtResponse(null, Constantes.usuarioBloqueado, Boolean.FALSE, null);
            }
            ActiveDirectoryModel exitoso = activeDirectory.getConnection(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            if (!exitoso.getEstado()) {//INTENTO FALLIDO
                UsuarioAcceso usuarioAcceso = usuarioAcessoService.guardarAcceso(usuario.getId(), headers.getFirst("ip"), headers.getFirst("os"), headers.getFirst("mac"), Constantes.FALLIDO);
                if (usuarioAcceso.getIntento().equals(Constantes.INTENTOS_SESION_DEFAULT)) {
                    return new JwtResponse(null, Constantes.usuarioBloqueado, Boolean.FALSE, null);
                } else {
                    return new JwtResponse(null, String.format(Constantes.usuarioFallido, usuarioAcceso.getIntento() + " de " + Constantes.INTENTOS_SESION_DEFAULT, Constantes.INTENTOS_SESION_DEFAULT), Boolean.FALSE, null);
                }
                //return new JwtResponse(null, Constantes.usuarioADCredencialError, Boolean.FALSE, null);
            } else {
                return inicioSesionWS(usuario.getId(), usuario.getUsuario(), authenticationRequest.getPassword(), headers, exitoso);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JwtResponse(null, String.format(Constantes.usuarioADNoEncontrado, authenticationRequest.getUsername()), Boolean.FALSE, null);
        }
    }

    public JwtResponse inicioSesionWS(Long idUsuario, String usuario, String clave, MultiValueMap<String, String> headers, ActiveDirectoryModel directoryModel) {
        usuarioAcessoService.guardarAcceso(idUsuario, headers.getFirst("ip"), headers.getFirst("os"), headers.getFirst("mac"), Constantes.CORRECTO);
        UserDetails userDetails = new User(usuario, clave, new ArrayList<>());
        String url = appProps.getUrlApp() + "/servicios/sas/api/iniciarSesion";
        String token = jwtTokenUtil.generateToken(userDetails);
        String js = "{\"usuario\" : \"" + usuario + "\", \"clave\" : \"" + clave + "\"   }";
        UsuarioInicioSesion object = (UsuarioInicioSesion) restService.restPOST(url, token, js, UsuarioInicioSesion.class);
        if (object != null) {
            System.out.println(url + " >>>>>>>" + new Gson().toJson(object));
        }
        return new JwtResponse(token, Constantes.usuarioActivo, Boolean.TRUE, new Gson().toJson(object), directoryModel);
    }

    public JwtResponse inicioSesionWS(JwtToken token, String usuario, String clave, MultiValueMap<String, String> headers, ActiveDirectoryModel directoryModel) {
        try {
//            usuarioAcessoService.guardarAcceso(null, headers.getFirst("ip"), headers.getFirst("os"), headers.getFirst("mac"), Constantes.CORRECTO);
        } catch (Exception c) {
            System.out.println("Error " + c.getMessage());
        }
        String url = appProps.getUrlApp() + "/servicios/sas/api/iniciarSesion";
        String js = "{\"usuario\" : \"" + usuario + "\", \"clave\" : \"" + clave + "\"   }";
        UsuarioInicioSesion object = (UsuarioInicioSesion) restService.restPOST(url, token.getToken(), js, UsuarioInicioSesion.class);
        System.out.println(new Gson().toJson(object));
        //object.setActiveDirectoryResponse(directoryModel);
        return new JwtResponse(token.getToken(), Constantes.usuarioActivo, Boolean.TRUE, new Gson().toJson(object), directoryModel);
    }
}
