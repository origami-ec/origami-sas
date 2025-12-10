package org.sas.zull.resources;

import org.sas.zull.security.JwtUserDetailsService;
import org.sas.zull.service.JwtRequest;
import org.sas.zull.service.JwtResponse;
import org.sas.zull.service.JwtToken;
import org.sas.zull.service.UsuarioService;
import org.sas.zull.util.Constantes;
import org.sas.zull.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@CrossOrigin
public class JwtAuthenticationResource {
    private static final Logger logger = Logger.getLogger(JwtAuthenticationResource.class.getName());
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(value = "/autenticar", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, @RequestHeader MultiValueMap<String, String> headers) {
        try {
            final JwtResponse userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername(), authenticationRequest.getPassword(), headers);
            return ResponseEntity.ok(userDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/autenticarAD", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationAD(@RequestBody JwtRequest authenticationRequest, @RequestHeader MultiValueMap<String, String> headers) {
        JwtResponse userDetails = userDetailsService.autenticarAD(authenticationRequest, headers);
        //  JwtResponse userDetails =  userDetailsService.autenticarADTodos(authenticationRequest, headers);
        return ResponseEntity.ok(userDetails);
    }


    @RequestMapping(value = "/validarToken", method = RequestMethod.POST)
    public ResponseEntity<?> validarToken(@RequestBody JwtToken token, @RequestHeader MultiValueMap<String, String> headers) {

        try {
            System.out.println("--> " + token.getToken());
            String username = jwtTokenUtil.getUsernameFromToken(token.getToken());
            if (username != null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(token.getToken(), userDetails)) {
                    JwtResponse jwtResponse = userDetailsService.inicioSesionWS(token, username, null, headers, null);
                    System.out.println("Token valido: " + jwtResponse);
                    return ResponseEntity.ok(jwtResponse);
                } else {
                    JwtResponse jwtResponse = new JwtResponse(token.getToken(), Constantes.FALLIDO, Boolean.FALSE, null);
                    System.out.println("Token invalido: ");
                    return ResponseEntity.ok(jwtResponse);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "validarToken", e);
        }
        return ResponseEntity.ok(new JwtResponse(token.getToken(), Constantes.FALLIDO, Boolean.FALSE, null));
    }

}
