package org.sas.seguridad.resource;

//import org.ibarra.seguridad.dto.*;
import org.sas.seguridad.dto.*;
import org.sas.seguridad.entity.Usuario;
import org.sas.seguridad.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UsuarioResource {

    @Autowired
    private UsuarioService service;

    @PostMapping("/iniciarSesion")
    public ResponseEntity<?> iniciarSesion(@RequestBody UsuarioInicioSesion data) {
        try {
            return new ResponseEntity<>(service.iniciarSesion(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/usuarioXpersona")
    public ResponseEntity<?> usuarioXpersona(@RequestBody RespuestaWs data) {
        try {
            return new ResponseEntity<>(service.usuarioXpersona(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/personaXusuario")
    public ResponseEntity<?> personaXusuario(@RequestBody RespuestaWs data) {
        try {
            return new ResponseEntity<>(service.personaXusuario(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/generar/codigo/verificacion/{idUsuario}")
    public ResponseEntity<?> generarCodigoVerificacion(@PathVariable Long idUsuario) {
        try {
            return new ResponseEntity<>(service.generarCodigoVerificacion(idUsuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Busca el usuario por numIdentificacion
     * @param data
     * @return
     */
    @PostMapping("/usuario/persona")
    public ResponseEntity<?> verificarUsuarioPersona(@RequestBody Data data) {
        try {
            return new ResponseEntity<>(service.validarUsuarioPersona(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/usuario/identificacion")
    public ResponseEntity<?> buscarUsuarioIdentificacion(@RequestBody Persona data) {
        try {
            return new ResponseEntity<>(service.buscarUsuarioIdentificacion(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/validar/codigo/verificacion")
    public ResponseEntity<?> validarCodigoVerificacion(@RequestBody UsuarioVerificacionDTO data) {
        try {
            return new ResponseEntity<>(service.verificarCodigo(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/usuario/consultar")
    public ResponseEntity<Usuario> find(@RequestBody Usuario data) {
        try {
            return new ResponseEntity<>(service.find(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/usuario/consultar/{code}")
    public ResponseEntity<?> find(@PathVariable Long code) {
        try {
            return new ResponseEntity<>(service.find(code), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "user/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> findAll(@Valid Usuario data) {
        try {
            System.out.println(data.toString());
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "user/findPorPersona/{idUsuario}")
    public ResponseEntity<Usuario> findAll(@PathVariable Long idUsuario) {
        try {
            System.out.println(idUsuario.toString());
            return new ResponseEntity<>(service.findByPersona(idUsuario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/usuarios/findAll/page")
    public ResponseEntity<?> findAll(@Valid Usuario data, Pageable pageable) {
        try {
            Map<String, List> map = service.findAll(data, pageable);
            List<String> pages = map.get("pages");
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("totalPages", pages.get(0));
            responseHeaders.add("rootSize", pages.get(1));
            return new ResponseEntity<>(map.get("result"), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuario/guardar", method = RequestMethod.POST)
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        try {
            return new ResponseEntity<>(service.guardar(usuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuario/editar", method = RequestMethod.POST)
    public ResponseEntity<?> editar(@RequestBody Usuario usuario) {
        try {
            return new ResponseEntity<>(service.editar(usuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuario/actualizar/clave", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarPassword(@RequestBody UsuarioInicioSesion data) {
        try {
            return new ResponseEntity<>(service.updatePassword(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuario/actualizar/clave/pers", method = RequestMethod.POST)
    public ResponseEntity<?> actualizarPassword1(@RequestBody UsuarioInicioSesion data) {
        try {
            return new ResponseEntity<>(service.updatePassword1(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuario/reenviarCorreoActivacion", method = RequestMethod.POST)
    public ResponseEntity<?> reenviarCorreoActivacion(@RequestBody Usuario usuario) {
        try {
            service.enviarCorreoVerificacion(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "usuario/correoCambioClave", method = RequestMethod.POST)
    public ResponseEntity<?> correoCambioClave(@RequestBody Usuario usuario) {
        try {
            return new ResponseEntity<>(service.correoCambioClave(usuario), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/usuario/ByPersonaId/{personaId}")
    public ResponseEntity<Usuario> findByPersonaIdAndEstado(@PathVariable Long personaId) {
        try {
            return new ResponseEntity<>(service.findByPersonaIdAndEstado(personaId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update/user")
    public ResponseEntity<?> updateUser(@RequestBody Usuario data) {
        try {
            return new ResponseEntity<>(service.updateUser(data), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
