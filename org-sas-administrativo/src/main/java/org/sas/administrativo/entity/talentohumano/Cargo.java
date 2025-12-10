package org.sas.administrativo.entity.talentohumano;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.util.EsquemaConfig;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cargo", schema = EsquemaConfig.talentoHumano)
public class Cargo implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "estado", nullable = true, length = -1)
    private String estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad", referencedColumnName = "id")
    private UnidadAdministrativa unidad;

    @Column(name = "codigo", nullable = true, length = -1)
    private String codigo;
    @Column(name = "activo", nullable = true)
    private Boolean activo;
    @Column(name = "asignado", nullable = true)
    private Boolean asignado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato", referencedColumnName = "id")
    private CatalogoItem contrato;
    private String partida;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_cargo", referencedColumnName = "id")
    private PerfilCargo perfilCargo;

    //    @Formula("(select case when coalesce(count(cr.id),0) > 0 then true else false end from talento_humano.cargo_rubro cr where cr.codigo_reforma is null and cr.reforma_borrador is null and cr.partida_presupuestaria is null and cr.cargo=id and cr.periodo=(select sg.anio from configuracion.secuencia_general sg where sg.code='ANIO_CARGO_DISTRIBUTIVO'))")
    @Transient
    private Boolean partidaAsignada;
    private Long proyectoEntregable;
    private String partidaAnterior;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_gasto", referencedColumnName = "id")
    private CatalogoItem tipoGasto;
    private String userCreacion;
    private String userModificacion;
    private Date fechaCreacion;
    private Date fechaModificacion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_banda", referencedColumnName = "id")
    private CatalogoItem tipoBanda;

    public Cargo() {
    }

    public Cargo(String nombreCargo, String estado, Long unidad, Long tipoContrato, Long programa) {
        this.estado = estado;
        if (nombreCargo != null) {
            this.perfilCargo = new PerfilCargo(nombreCargo);
        } else {
            this.perfilCargo = null;
        }

        if (unidad != null) {
            this.unidad = new UnidadAdministrativa(unidad);
        } else {
            this.unidad = null;
        }
        if (tipoContrato != null) {
            this.contrato = new CatalogoItem(tipoContrato);
        } else {
            this.contrato = null;
        }

        if (estado != null) {
            this.estado = estado;
        } else {
            this.estado = null;
        }
    }

    public Cargo(Long programa, Long centroCosto, Long tipoContrato) {

        if (tipoContrato != null) {
            this.contrato = new CatalogoItem(tipoContrato);
        }
    }


    public Cargo(Long id) {
        this.id = id;
    }

    public Cargo(Long id, Long idPerfil, String nombrePerfilCargo) {
        this.id = id;
        this.perfilCargo = new PerfilCargo(idPerfil, nombrePerfilCargo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public CatalogoItem getContrato() {
        return contrato;
    }

    public void setContrato(CatalogoItem contrato) {
        this.contrato = contrato;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public PerfilCargo getPerfilCargo() {
        return perfilCargo;
    }

    public void setPerfilCargo(PerfilCargo perfilCargo) {
        this.perfilCargo = perfilCargo;
    }

    public Boolean getPartidaAsignada() {
        return partidaAsignada;
    }

    public void setPartidaAsignada(Boolean partidaAsignada) {
        this.partidaAsignada = partidaAsignada;
    }

    public Long getProyectoEntregable() {
        return proyectoEntregable;
    }

    public void setProyectoEntregable(Long proyectoEntregable) {
        this.proyectoEntregable = proyectoEntregable;
    }

    public String getPartidaAnterior() {
        return partidaAnterior;
    }

    public void setPartidaAnterior(String partidaAnterior) {
        this.partidaAnterior = partidaAnterior;
    }

    public CatalogoItem getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(CatalogoItem tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public String getUserModificacion() {
        return userModificacion;
    }

    public void setUserModificacion(String userModificacion) {
        this.userModificacion = userModificacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public CatalogoItem getTipoBanda() {
        return tipoBanda;
    }

    public void setTipoBanda(CatalogoItem tipoBanda) {
        this.tipoBanda = tipoBanda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        return id.equals(cargo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                '}';
    }
}
