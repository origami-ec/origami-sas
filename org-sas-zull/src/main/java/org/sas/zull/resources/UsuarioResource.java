package org.sas.zull.resources;

import org.sas.zull.repository.UsuarioRepository;
import org.sas.zull.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("zuul/cache/")
public class UsuarioResource {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PutMapping("users/update")
    public ResponseEntity<Usuario> updateUser(@RequestBody Usuario user) {
        usuarioRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("users/create")
    public ResponseEntity<Usuario> createUser(@RequestBody Usuario user) {
        usuarioRepository.save(user);
        return ResponseEntity.ok(user);
    }
    
}
