package org.sas.zull.service;

import org.sas.zull.repository.UsuarioRepository;
import org.sas.zull.util.Constantes;
import org.sas.zull.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario find(Usuario data) {
        System.out.println(data.toString());
        return repository.findOne(Example.of(data)).orElse(null);
    }
    public Usuario findXactiveDirectory(String activeDirectory) {
        return repository.findByActiveDirectoryAndEstado(activeDirectory, Constantes.ACTIVO);
    }


    public Usuario findUsuario(String usuario) {
        return repository.findByUsuarioAndEstado(usuario, Constantes.ACTIVO);
    }

    public Usuario findById(Long id) {
        return repository.getOne(id);
    }

    public Usuario actualizar(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> findAll(Usuario data) {
        return repository.findAll(Example.of(data));
    }

    public Usuario consultarUsuario(String username) {
        Usuario u = repository.findByUsuario(username);
        Usuario response = new Usuario();
        response.setId(u.getId());
        response.setUsuario(u.getUsuario());
        return response;
    }
}
