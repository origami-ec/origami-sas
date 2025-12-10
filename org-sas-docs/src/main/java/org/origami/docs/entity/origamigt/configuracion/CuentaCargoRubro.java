package org.origami.docs.entity.origamigt.configuracion;

public class CuentaCargoRubro {

    private Long idRubro;
    private Long idCuentaContable;
    private Short periodo;
    private Long idContrato;
    private Long idClasificacion;
    private Long idRegimenLaboral;

    public CuentaCargoRubro() {
    }

    public CuentaCargoRubro(Long idRubro, Long idCuentaContable, Short periodo, Long idContrato, Long idClasificacion, Long idRegimenLaboral) {
        this.idRubro = idRubro;
        this.idCuentaContable = idCuentaContable;
        this.periodo = periodo;
        this.idContrato = idContrato;
        this.idClasificacion = idClasificacion;
        this.idRegimenLaboral = idRegimenLaboral;
    }

    public Long getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public Long getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(Long idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(Long idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public Long getIdRegimenLaboral() {
        return idRegimenLaboral;
    }

    public void setIdRegimenLaboral(Long idRegimenLaboral) {
        this.idRegimenLaboral = idRegimenLaboral;
    }

    @Override
    public String toString() {
        return "CuentaCargoRubroDto{" + "idRubro=" + idRubro + ", idCuentaContable=" + idCuentaContable + ", periodo=" + periodo + ", idContrato=" + idContrato + ", idClasificacion=" + idClasificacion + ", idRegimenLaboral=" + idRegimenLaboral + '}';
    }

}
