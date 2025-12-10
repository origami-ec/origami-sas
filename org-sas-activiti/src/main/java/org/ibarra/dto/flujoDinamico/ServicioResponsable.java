/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ibarra.dto.flujoDinamico;

import java.util.Objects;

/**
 * @author asanc
 */
public class ServicioResponsable {

    private Long id;
    private Long servidor;
    private String responsable;
    private String nombre;
    private String usuarioRegistro;
    private String usuarioModifica;
    private ServicioTarea servicioTarea;
    private ServidorCargoDto servidorCargo;

    public ServicioResponsable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServidor() {
        return servidor;
    }

    public void setServidor(Long servidor) {
        this.servidor = servidor;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public ServicioTarea getServicioTarea() {
        return servicioTarea;
    }

    public void setServicioTarea(ServicioTarea servicioTarea) {
        this.servicioTarea = servicioTarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ServidorCargoDto getServidorCargo() {
        return servidorCargo;
    }

    public void setServidorCargo(ServidorCargoDto servidorCargo) {
        this.servidorCargo = servidorCargo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServicioResponsable other = (ServicioResponsable) obj;
        return Objects.equals(this.id, other.id);
    }


}
