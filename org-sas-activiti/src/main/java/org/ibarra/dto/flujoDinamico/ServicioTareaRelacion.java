/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ibarra.dto.flujoDinamico;

/**
 * @author asanc
 */
public class ServicioTareaRelacion {

    private Long id;
    private Boolean tipo; // true sucesora false antecesora
    private String usuarioRegistro;
    private String usuarioModifica;
    private ServicioTarea servicioTarea;
    private ServicioTarea destino;

    public ServicioTareaRelacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
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

    public ServicioTarea getDestino() {
        return destino;
    }

    public void setDestino(ServicioTarea destino) {
        this.destino = destino;
    }

}
