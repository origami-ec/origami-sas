package org.ibarra.entity;


import jakarta.persistence.*;
import org.ibarra.util.EsquemaConfig;

import java.util.Date;


@Entity
@Table(name = "documentos_tramites", schema = EsquemaConfig.procesos)
public class DocumentoTramite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "hi_tramite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private HistoricoTramite tramite;
    @JoinColumn(name = "requisito", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoTramiteRequisito requisito;
    private String descripcion;
    private String referenciaDoc;
    private String claseName;
    private String idReferencia;
    private String usuarioRegistro;
    private Date fechaRegistro;

    public DocumentoTramite() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public TipoTramiteRequisito getRequisito() {
        return requisito;
    }

    public void setRequisito(TipoTramiteRequisito requisito) {
        this.requisito = requisito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferenciaDoc() {
        return referenciaDoc;
    }

    public void setReferenciaDoc(String referenciaDoc) {
        this.referenciaDoc = referenciaDoc;
    }

    public String getClaseName() {
        return claseName;
    }

    public void setClaseName(String claseName) {
        this.claseName = claseName;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        this.idReferencia = idReferencia;
    }

    public HistoricoTramite getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramite tramite) {
        this.tramite = tramite;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "DocumentoTramite{" +
                "id=" + id +
                ", requisito=" + requisito +
                ", descripcion='" + descripcion + '\'' +
                ", referenciaDoc='" + referenciaDoc + '\'' +
                ", claseName='" + claseName + '\'' +
                ", idReferencia='" + idReferencia + '\'' +
                '}';
    }
}
