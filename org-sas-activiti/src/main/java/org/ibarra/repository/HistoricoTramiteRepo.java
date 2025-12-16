package org.ibarra.repository;

import org.ibarra.dto.TareasActivas;
import org.ibarra.entity.HistoricoTramite;
import org.ibarra.entity.TipoTramite;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HistoricoTramiteRepo extends JpaRepository<HistoricoTramite, Long> {

    HistoricoTramite findByIdProceso(String proceso);

    Integer countByIdProceso(String proceso);

    HistoricoTramite findFirstByTramiteOrderByIdDesc(String tramite);

    HistoricoTramite findByReferenciaIdAndReferencia(Long referenciaId, String referencia);

    List<HistoricoTramite> findAllByTipoTramite_IdAndPeriodoOrderByIdDesc(Long tipoTramite, Integer periodo);

    Page<HistoricoTramite> findAllByTipoTramite_AbreviaturaAndPeriodoOrderByIdDesc(String tipoTramite, Integer periodo, Pageable pageable);

    Page<HistoricoTramite> findAllByPeriodoOrderByIdDesc(Integer periodo, Pageable pageable);

    @Query(value = "SELECT distinct s.idProceso FROM HistoricoTramite s WHERE s.tramite IN (?1)  AND s.idProceso IS NOT NULL")
    List<String> findAllByTramiteIN(List<String> tramites);

    @Query(value = "SELECT distinct s.idProceso FROM HistoricoTramite s WHERE upper(s.tramite) like upper(?1) AND s.idProceso IS NOT NULL")
    List<String> findAllTramite(String tramite);

    @Query("SELECT h.idProceso FROM HistoricoTramite h WHERE h.tramite = :tramite")
    List<String> findProcessIdsByTramite(@Param("tramite") String tramite);


    @Override
    @Cacheable(value = "historicoTramite", key = "#aLong")
    Optional<HistoricoTramite> findById(Long aLong);

    List<HistoricoTramite> findByTipoTramiteVentanillaPublica(Boolean ventanilla);

    List<HistoricoTramite> findByTipoTramite(TipoTramite tipoTramite);

    List<HistoricoTramite> findAllByFechaIngresoBetween(Date inicio, Date fin);

    @Query(value = "SELECT DISTINCT ti.id_ as task_id, pi.proc_inst_id_ proc_inst_id, ti.task_def_key_ task_def_key, ti.name_ \"name\", " +
            "    ti.assignee_ assignee, ti.create_time_ create_time, ti.form_key_ form_key, ti.priority_ priority, " +
            "    (SELECT user_id_ FROM public.act_ru_identitylink WHERE task_id_ = ti.id_ limit 1) AS candidate, \n" +
            "   ti.rev_ rev, \n" +
            "   ht.id, \n" +
            "   ht.id id_tramite,\n" +
            "   ht.id AS num_tramite,\n" +
            "   ht.fecha AS fecha_ingreso,\n" +
            "   ht.fecha AS fecha_entrega, \n" +
            "   null::text as nombre_propietario,\n" +
            "   ht.tramite id_proceso_temp, \n" +
            "   case when ht.estado = true then 'Pendiente' else 'Finalizado' end AS entregado, \n" +
            "   ht.tramite carpeta_rep,\n" +
            "   tt.id AS id_tipo_tramite,\n" +
            "   tt.descripcion, \n" +
            "   ht.referencia_id \n" +
            "FROM act_hi_procinst pi LEFT JOIN act_hi_procinst pis ON (pis.super_process_instance_id_ = pi.id_) \n" +
            "INNER JOIN procesos.historico_tramite ht ON ht.id_proceso::text = pi.proc_inst_id_::text \n" +
            "INNER JOIN procesos.tipo_tramite tt on ht.tipo_tramite = tt.id \n" +
            "INNER JOIN act_ru_task ti ON ((ti.proc_inst_id_ = pi.proc_inst_id_ AND pi.proc_def_id_ = ti.proc_def_id_) OR (ti.proc_inst_id_ = pis.proc_inst_id_ AND pis.proc_def_id_ = ti.proc_def_id_)) \n" +
            "WHERE Upper(ti.assignee_) = ?1 order by ti.create_time_ desc"
            , nativeQuery = true
            , countQuery = "SELECT count(distinct ti.ID_) \n" +
            "FROM  public.act_hi_procinst pi LEFT JOIN public.act_hi_procinst pis ON (pis.super_process_instance_id_ = pi.id_) \n" +
            "INNER JOIN procesos.historico_tramite ht ON ht.id_proceso::text = pi.proc_inst_id_::text \n" +
            "INNER JOIN procesos.tipo_tramite tt on ht.tipo_tramite = tt.id \n" +
            "INNER JOIN public.act_ru_task ti ON ((ti.proc_inst_id_ = pi.proc_inst_id_ AND pi.proc_def_id_ = ti.proc_def_id_) OR (ti.proc_inst_id_ = pis.proc_inst_id_ AND pis.proc_def_id_ = ti.proc_def_id_)) \n" +
            "WHERE Upper(ti.assignee_) = ?1")
    Page<TareasActivas> findAllTareasActivasPage(String usuario, Pageable pageable);

    @Query(value = "SELECT DISTINCT ti.id_ as task_id, pi.proc_inst_id_ proc_inst_id, ti.task_def_key_ task_def_key, ti.name_ \"name\", " +
            "    ti.assignee_ assignee, ti.create_time_ create_time, ti.form_key_ form_key, ti.priority_ priority, " +
            "    (SELECT user_id_ FROM public.act_ru_identitylink WHERE task_id_ = ti.id_ limit 1) AS candidate, \n" +
            "   ti.rev_ rev, \n" +
            "   ht.id, \n" +
            "   ht.id id_tramite,\n" +
            "   ht.id AS num_tramite,\n" +
            "   ht.fecha AS fecha_ingreso,\n" +
            "   ht.fecha AS fecha_entrega, \n" +
            "   null::text as nombre_propietario,\n" +
            "   ht.tramite id_proceso_temp, \n" +
            "   case when ht.estado = true then 'Pendiente' else 'Finalizado' end AS entregado, \n" +
            "   ht.tramite carpeta_rep,\n" +
            "   tt.id AS id_tipo_tramite,\n" +
            "   tt.descripcion, \n" +
            "   ht.referencia_id \n" +
            "FROM act_hi_procinst pi LEFT JOIN act_hi_procinst pis ON (pis.super_process_instance_id_ = pi.id_) \n" +
            "INNER JOIN procesos.historico_tramite ht ON ht.id_proceso::text = pi.proc_inst_id_::text \n" +
            "INNER JOIN procesos.tipo_tramite tt on ht.tipo_tramite = tt.id \n" +
            "INNER JOIN act_ru_task ti ON ((ti.proc_inst_id_ = pi.proc_inst_id_ AND pi.proc_def_id_ = ti.proc_def_id_) OR (ti.proc_inst_id_ = pis.proc_inst_id_ AND pis.proc_def_id_ = ti.proc_def_id_)) \n" +
            "WHERE Upper(ti.assignee_) = ?1 AND tt.tramite = ?2 order by ti.create_time_ desc"
            , nativeQuery = true
            , countQuery = "SELECT count(distinct ti.ID_) \n" +
            "FROM  public.act_hi_procinst pi LEFT JOIN public.act_hi_procinst pis ON (pis.super_process_instance_id_ = pi.id_) \n" +
            "INNER JOIN procesos.historico_tramite ht ON ht.id_proceso::text = pi.proc_inst_id_::text \n" +
            "INNER JOIN procesos.tipo_tramite tt on ht.tipo_tramite = tt.id \n" +
            "INNER JOIN public.act_ru_task ti ON ((ti.proc_inst_id_ = pi.proc_inst_id_ AND pi.proc_def_id_ = ti.proc_def_id_) OR (ti.proc_inst_id_ = pis.proc_inst_id_ AND pis.proc_def_id_ = ti.proc_def_id_)) \n" +
            "WHERE Upper(ti.assignee_) = ?1 AND tt.tramite = ?2")
    Page<TareasActivas> findAllTareasActivasDepartamentoPage(String usuario, Long departamento, Pageable pageable);

    Page<HistoricoTramite> findAllByFechaIngresoOrderByIdDesc(
            Date fechaIngreso, Pageable pageable);

    Page<HistoricoTramite> findAllBySolicitante_NumIdentificacionOrderByIdDesc(
            String numIdentificacion,
            Pageable pageable
    );

}
