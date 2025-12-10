package org.sas.zull.service;

public class JwtDesk {

    private final String jwttoken;
    private final Long id;
    private final String usuario;

    public JwtDesk(String jwttoken, Long id, String usuario) {
        this.jwttoken = jwttoken;
        this.id = id;
        this.usuario = usuario;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

}
