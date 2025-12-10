package org.origami.docs.repository;

import org.origami.docs.entity.Indexacion;
import org.origami.docs.entity.ListadoMaestroIndexacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ListadoMaestroIndexacionRepository  extends MongoRepository<ListadoMaestroIndexacion, String> {
    ListadoMaestroIndexacion findByDescripcion(String descripcion);

    ListadoMaestroIndexacion findByCodigoAndEstado(String codigo, Boolean estado);

    List<ListadoMaestroIndexacion> findAllByEstado(Boolean estado);
}
