package org.sas.seguridad.service;

import org.sas.seguridad.dto.RespuestaWs;
import org.sas.seguridad.dto.ServidorDatos;
import org.sas.seguridad.dto.UsuarioDetalle;
import org.sas.seguridad.entity.Rol;
import org.sas.seguridad.entity.Usuario;
import org.sas.seguridad.entity.UsuarioRol;
import org.sas.seguridad.repository.UsuarioRolRepo;
import org.sas.seguridad.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioRolService {

    @Autowired
    private UsuarioRolRepo _usuarioRolRepo;
    @Autowired
    private AdministrativoService administrativoService;
    @Autowired
    private UsuarioService usuarioService;

    public Boolean usuarioXrol(String usuario, String rol) {
        UsuarioRol usuarioRol = _usuarioRolRepo.usuarioXrol(usuario, rol);
        return usuarioRol != null ? Boolean.TRUE : Boolean.FALSE;
    }

    public List<Long> getRolesUsuario(String usuario) {
        return _usuarioRolRepo.getRolesByUser(usuario);
    }

    public List<String> rolesXUsuario(String usuario) {
        return _usuarioRolRepo.rolesXUsuario(usuario);
    }

    public UsuarioRol find(UsuarioRol data) {
        List<UsuarioRol> dataBD = _usuarioRolRepo.findAll(Example.of(data));
        if (!dataBD.isEmpty()) {
            return dataBD.get(0);
        } else {
            return null;
        }
    }

    public UsuarioRol save(UsuarioRol data) {
        return _usuarioRolRepo.save(data);
    }

    public List<UsuarioRol> findAll(UsuarioRol data) {
        if (data.getUsuario() == null && data.getRol() == null) {
            return null;
        }
        return _usuarioRolRepo.findAll(Example.of(data), Sort.by(Sort.Direction.ASC, "usuario.id"));
    }

    public Map<String, List> findAll(UsuarioRol data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<UsuarioRol> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = _usuarioRolRepo.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(_usuarioRolRepo.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public List<UsuarioDetalle> consultarUsuarioXrol(String rol) {
        List<Usuario> usuariosRoles = _usuarioRolRepo.consultarUsuarioXrol(rol.toUpperCase());
        System.out.println("usuariosRoles "+usuariosRoles.size());
        List<UsuarioDetalle> usuarios = new ArrayList<>();
        if (Utils.isNotEmpty(usuariosRoles)) {
            usuariosRoles = usuariosRoles.stream().distinct().collect(Collectors.toList());
            for (Usuario u : usuariosRoles) {
                UsuarioDetalle usuario = new UsuarioDetalle();
                usuario.setId(u.getId());
                usuario.setUsuario(u.getUsuario());
                usuario.setEstado(u.getEstado());
                ServidorDatos sd = administrativoService.findServidorDatos(new RespuestaWs(u.getPersonaId()));
                usuario.setServidor(sd);
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public UsuarioDetalle consultarUsuario(String nameuser) {
        Usuario  user = usuarioService.findByUsuario(nameuser);
        UsuarioDetalle usuario = new UsuarioDetalle();
        usuario.setId(user.getId());
        usuario.setUsuario(user.getUsuario());
        usuario.setEstado(user.getEstado());
        ServidorDatos sd = administrativoService.findServidorDatos(new RespuestaWs(user.getPersonaId()));
        usuario.setServidor(sd);

        return usuario;
    }

    public List<UsuarioRol> saveAll(List<UsuarioRol> data) {
        List<UsuarioRol> rolUsuarioDB = new ArrayList<>();
        for (UsuarioRol r : data) {
            if (find(new UsuarioRol(new Rol(r.getRol().getId()), new Usuario(r.getUsuario().getId()))) == null) {
                rolUsuarioDB.add(save(r));
            }
        }
        return rolUsuarioDB;
    }

    public void deleteAll(List<UsuarioRol> datas) {
        for (UsuarioRol ru : datas) {
            UsuarioRol rDB = find(new UsuarioRol(new Rol(ru.getRol().getId()), new Usuario(ru.getUsuario().getId())));
            if (rDB != null) {
                delete(rDB);
            }
        }
    }

    public void delete(UsuarioRol data) {
        _usuarioRolRepo.delete(data);
    }
}
