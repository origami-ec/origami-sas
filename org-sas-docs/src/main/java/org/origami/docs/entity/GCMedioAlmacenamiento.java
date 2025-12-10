package org.origami.docs.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gcMediaAlmacenamiento")
public class GCMedioAlmacenamiento {
    @Id
    public String _id;
    public String medioAlmacenamiento;
    public Usuario usuario;

    public GCMedioAlmacenamiento() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMedioAlmacenamiento() {
        return medioAlmacenamiento;
    }

    public void setMedioAlmacenamiento(String medioAlmacenamiento) {
        this.medioAlmacenamiento = medioAlmacenamiento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
