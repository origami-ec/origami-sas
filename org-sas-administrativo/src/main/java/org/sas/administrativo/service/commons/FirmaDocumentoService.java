package org.sas.administrativo.service.commons;

import org.sas.administrativo.conf.AppProps;
import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.dto.commons.FirmaDocumentoDto;
import org.sas.administrativo.dto.documental.ArchivoIndexDto;
import org.sas.administrativo.entity.FirmaDocumento;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.mapper.commons.FirmaDocumentoMapper;
import org.sas.administrativo.repository.FirmaDocumentoRepository;
import org.sas.administrativo.repository.configuracion.CatalogoItemRepository;
import org.sas.administrativo.service.configuracion.ReporteService;
import org.sas.administrativo.util.Constantes;
import org.sas.administrativo.util.Utils;
import org.sas.administrativo.util.ValoresCodigo;
import org.sas.administrativo.util.model.EstadoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FirmaDocumentoService {
    private static final Logger logger = Logger.getLogger(FirmaDocumentoService.class.getName());
    @Autowired
    private AppProps appProps;
    @Autowired
    private FirmaDocumentoRepository firmaDocumentoRepository;
    @Autowired
    private FirmaDocumentoMapper firmaDocumentoMapper;
    @Autowired
    private CatalogoItemRepository itemRepo;
    @Autowired
    private ReporteService reporteService;
    @Autowired
    private FirmaDocumentoRepository firmaDocumentoRepo;

    public FirmaDocumento guardar(FirmaDocumento documentos) {
        inactivarFirmaDocumentos(documentos.getReferencia(), documentos.getTipo().getId(), documentos.getEstado().getId());
        return firmaDocumentoRepository.save(documentos);
    }

    public List<FirmaDocumento> guardar(List<FirmaDocumento> documentos) {
        System.out.println("Llega>>>");
        try {
            List<FirmaDocumento> list = new ArrayList<>(documentos.size());
            for (FirmaDocumento fd : documentos) {
                if (fd.getTipo() != null && fd.getTipo().getId() == null) {
                    fd.setTipo(null);
                }
                if (fd.getEstado() == null) {
                    fd.setEstado(new CatalogoItem(appProps.getFirmaPendienteId()));
                }
                list.add(fd);
                inactivarFirmaDocumentos(fd.getReferencia(), (fd.getTipo() == null ? null : fd.getTipo().getId()), fd.getEstado().getId());
            }

            return firmaDocumentoRepository.saveAll(list);
            /*List<FirmaDocumento> o = new ArrayList<>(documentos.size());
            for (FirmaDocumento f : documentos) {

                FirmaDocumento save = firmaDocumentoRepository.save(f);
                o.add(save);
            }
            return o;*/
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public List<FirmaDocumento> guardarFirmaDocumentoNotificacion(List<FirmaDocumento> documentos) {
        System.out.println("Llega de notificacion>>>");
        try {
            List<FirmaDocumento> list = new ArrayList<>(documentos.size());
            for (FirmaDocumento fd : documentos) {
                fd.setModulo(ValoresCodigo.NOTIFICACION);
                if (fd.getTipo() != null && fd.getTipo().getId() == null) {
                    fd.setTipo(null);
                }
                if (fd.getEstado() == null) {
                    fd.setEstado(new CatalogoItem(appProps.getFirmaPendienteId()));
                }
                list.add(fd);
                inactivarFirmaDocumentos(fd.getReferencia(), (fd.getTipo() == null ? null : fd.getTipo().getId()), fd.getEstado().getId());
            }

            return firmaDocumentoRepository.saveAll(list);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            return null;
        }
    }

    public void inactivarFirmaDocumentos(Long referencia, Long tipo, Long estado) {
        try {
            logger.info("referencia: " + referencia + " tipo: " + tipo + " estado: " + estado);
            List<FirmaDocumento> list = firmaDocumentoRepository.findAllByReferenciaAndTipo_IdAndEstado_IdIn(referencia, tipo, List.of(estado));
            if (Utils.isNotEmpty(list)) {
                firmaDocumentoRepository.deleteAll(list);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }

    public RespuestaWs verificarDocumentos(FirmaDocumentoDto dto) {

        List<FirmaDocumento> list = firmaDocumentoRepository.findAllByReferenciaAndTipo_IdAndEstado_IdIn(dto.getReferencia(), itemRepo.findByCodigoAndEstado(dto.getTipo().getCodigo(), EstadoType.ACTIVO.name()).getId(), List.of(appProps.getFirmaPendienteId()));
        if (Utils.isEmpty(list)) {
            return new RespuestaWs(Boolean.TRUE, null, Constantes.OK_MENSAJE);
        } else {
            return new RespuestaWs(Boolean.FALSE, Utils.gsonTransform(firmaDocumentoMapper.toDto(list)), Constantes.faltanDocumentos);
        }
    }

    public RespuestaWs consultarDocumentos(FirmaDocumentoDto dto) {

        List<FirmaDocumento> list = firmaDocumentoRepository.findAllByReferenciaAndTipo_IdOrderByIdDesc(dto.getReferencia(), itemRepo.findByCodigoAndEstado(dto.getTipo().getCodigo(), EstadoType.ACTIVO.name()).getId());
        if (Utils.isEmpty(list)) {
            return new RespuestaWs(Boolean.TRUE, null, Constantes.OK_MENSAJE);
        } else {
            return new RespuestaWs(Boolean.FALSE, Utils.gsonTransform(firmaDocumentoMapper.toDto(list)), Constantes.faltanDocumentos);
        }
    }

    public List<FirmaDocumento> consultarDocumentosXservidor(Long servidor) {
        List<FirmaDocumento> list = firmaDocumentoRepository.findAllByServidorAndEstado_IdOrderByIdDesc(servidor, appProps.getFirmaPendienteId());
        if (Utils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }

    public void actualizarPendientesXreferencia(Long referencia, String documentoNuevo, Long tipo, Long estado) {
        try {
            logger.log(Level.INFO, "referencia: " + referencia + " tipo: " + tipo + " estado: " + estado + " documentoNuevo: " + documentoNuevo);
            List<FirmaDocumento> list = firmaDocumentoRepository.findAllByReferenciaAndTipo_IdAndEstado_IdIn(referencia, tipo, List.of(estado,appProps.getFirmaFinalizadaId()));
            list.forEach(c -> c.setDocumento(documentoNuevo));
            firmaDocumentoRepository.saveAll(list);
            logger.log(Level.INFO, "tipo: " + tipo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public RespuestaWs actualizarFirmaDocumentos(FirmaDocumentoDto dto) {
        try {
            FirmaDocumento firmaDocumento = firmaDocumentoMapper.toEntity(dto);
            firmaDocumento.setFechaFirma(new Date());
            firmaDocumento.setEstado(new CatalogoItem(appProps.getFirmaFinalizadaId()));
            guardar(firmaDocumento);
            //ACTUALIZAR LOS DEMAS DOCUMENTOS
            actualizarPendientesXreferencia(dto.getReferencia(), dto.getDocumento(), dto.getTipo().getId(), dto.getEstado().getId());
            // guardar(firmaDocumento);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.OK_MENSAJE);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    public RespuestaWs actualizarFirmaDocumentosPendientes(List<FirmaDocumentoDto> dtoList) {
        List<FirmaDocumento> firmaDocumentos = firmaDocumentoMapper.toEntity(dtoList);
        try {
            logger.info("actualizarFirmaDocumentosPendientes " + firmaDocumentos.size());
            for (FirmaDocumento firmaDocumento : firmaDocumentos) {
                firmaDocumento.setFechaFirma(new Date());
                firmaDocumento.setEstado(new CatalogoItem(appProps.getFirmaFinalizadaId()));
                firmaDocumentoRepository.save(firmaDocumento);
            }
            return new RespuestaWs(Boolean.TRUE, null, Constantes.OK_MENSAJE);
        } catch (Exception e) {
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    public RespuestaWs generarFirmaDocumentoDto(List<FirmaDocumentoDto> dtolist, String documento, Long tipoId, Long estadoId) {
        try {
            List<FirmaDocumento> list = firmaDocumentoMapper.toEntity(dtolist);
            setValoresFirmaDocumento(list, documento, tipoId, estadoId);
            guardar(list);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.generaFirmaDocumento);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    public RespuestaWs generarFirmaDocumentoEntity(List<FirmaDocumento> list, String documento, Long tipoId, Long estadoId) {
        try {
            setValoresFirmaDocumento(list, documento, tipoId, estadoId);
            guardar(list);
            return new RespuestaWs(Boolean.TRUE, null, Constantes.generaFirmaDocumento);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);
        }
    }

    private void setValoresFirmaDocumento(List<FirmaDocumento> list, String documento, Long tipoId, Long estadoId) {
        for (FirmaDocumento firmaDocumento : list) {
            //inactivarFirmaDocumentos(firmaDocumento.getReferencia(), tipoId, estadoId);
            firmaDocumento.setDocumento(documento);
            firmaDocumento.setFecha(new Date());
            firmaDocumento.setTipo(new CatalogoItem(tipoId));
            firmaDocumento.setEstado(new CatalogoItem(estadoId));

        }
    }

    public FirmaDocumento firmanteDocumento(Long referencia, String usuario, String motivo, String palabraDesde, String palabraHasta, Integer movX, Integer movY, Long servidor) {
        FirmaDocumento firmaDocumento = new FirmaDocumento();
        firmaDocumento.setReferencia(referencia);
        firmaDocumento.setUsuario(usuario);
        firmaDocumento.setMotivo(motivo);
        firmaDocumento.setMovimientoX(movX);
        firmaDocumento.setMovimientoY(movY);
        firmaDocumento.setPalabraDesde(palabraDesde);
        firmaDocumento.setPalabraHasta(palabraHasta);
        firmaDocumento.setServidor(servidor);
        return firmaDocumento;
    }

    public FirmaDocumento firmanteDocumento(Long referencia, String usuario, String motivo, String palabraDesde, String palabraHasta, Integer movX, Integer movY, Long servidor, Boolean manual) {
        FirmaDocumento firmaDocumento = new FirmaDocumento();
        firmaDocumento.setReferencia(referencia);
        firmaDocumento.setUsuario(usuario);
        firmaDocumento.setMotivo(motivo);
        firmaDocumento.setMovimientoX(movX);
        firmaDocumento.setMovimientoY(movY);
        firmaDocumento.setPalabraDesde(palabraDesde);
        firmaDocumento.setPalabraHasta(palabraHasta);
        firmaDocumento.setServidor(servidor);
        firmaDocumento.setManual(manual);
        return firmaDocumento;
    }

    public FirmaDocumento firmanteDocumento(Long referencia, String usuario, String motivo, String palabraDesde, String palabraHasta, Integer movX, Integer movY, Long servidor, String tramite) {
        FirmaDocumento firmaDocumento = firmanteDocumento(referencia, usuario, motivo, palabraDesde, palabraHasta, movX, movY, servidor);
        firmaDocumento.setTramite(tramite);

        return firmaDocumento;
    }

    public RespuestaWs verifcarDocumentoFirmado(String tipo, Long referencia, String estado) {
        List<FirmaDocumento> result = firmaDocumentoRepo.findAllByReferenciaAndTipo_CodigoAndEstado_Codigo(referencia, tipo, estado);
        if (Utils.isNotEmpty(result)) {
            return new RespuestaWs(Boolean.TRUE, null, "Tiene que firmar el documento");
        }
        return new RespuestaWs(Boolean.FALSE, null, "");
    }

    /**
     * list LA LISTA TIENE LA MISMA INFORMACION SOLO QUE LO QUE LA HACE DIFERENTE ES LA PERSONA QUIEN FIRMA
     *
     * @return listado d firmantes
     * <p>
     * public RespuestaWs generarFirmaDocumento(List<FirmaDocumentoDto> list) {
     * String documento = generarReporte(list.get(0));
     * if (Utils.isNotEmptyString(documento)) {
     * return generarFirmaDocumentoDto(list, documento, null, appProps.getFirmaPendienteId());
     * return null;
     * } else {
     * return new RespuestaWs(Boolean.FALSE, null, Constantes.noGeneraDocumento);
     * }
     * }
     * <p>
     * public String generarReporte(FirmaDocumentoDto firmaDocumento, String nombreReporte) {
     * <p>
     * Map<String, Object> parametros = new HashMap<>();
     * parametros.put("ID", firmaDocumento.getReferencia().intValue());
     * <p>
     * DatosReporte datosReporte = new DatosReporte();
     * datosReporte.setDataSource(Boolean.FALSE);
     * datosReporte.setFormato(ReporteFormato.PDF.getCodigo());
     * datosReporte.setNombreReporte(nombreReporte);
     * <p>
     * datosReporte.setNombreArchivo(firmaDocumento.getMotivo() + ReporteFormato.PDF.getExtension());
     * datosReporte.setParametros(parametros);
     * byte[] reporte = reporteService.generarReporte(datosReporte);
     * if (reporte != null) {
     * Map<String, String> campos = new HashMap<>();
     * campos.put("CODIGO", "");
     * campos.put("PROPIETARIO", "");
     * campos.put("IDENTIFICACION", "");
     * ArchivoDocs archivoDocs = guardarGestorDocumental(firmaDocumento.getReferencia(), firmaDocumento.getMotivo(), firmaDocumento.getUsuarioDocs(), reporte);
     * if (archivoDocs != null) {
     * return archivoDocs.getId();
     * }
     * }
     * return null;
     * }
     */
    public List<FirmaDocumento> findByServidorAndEstadoAnTipoFirma(FirmaDocumento firma) {
        List<FirmaDocumento> list = firmaDocumentoRepository.findByServidorAndEstado_CodigoAndTipo_CodigoAndReferencia(firma.getServidor(), firma.getEstado().getCodigo(), firma.getTipo().getCodigo(), firma.getReferencia());
        if (Utils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }

    public List<FirmaDocumento> findByServidorAndTramite(FirmaDocumento firma) {
        System.out.println("estado:" + firma.getEstado().getCodigo());
        System.out.println("tipo:" + firma.getTipo().getCodigo());
        System.out.println("servidor:" + firma.getServidor());
        System.out.println("tramite:" + firma.getTramite());
        List<FirmaDocumento> list = firmaDocumentoRepository.findByServidorAndEstado_CodigoAndTipo_CodigoAndTramite(firma.getServidor(), firma.getEstado().getCodigo(), firma.getTipo().getCodigo(), firma.getTramite());
        if (Utils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }

    public RespuestaWs save(FirmaDocumento data) {
        RespuestaWs resp = new RespuestaWs();
        FirmaDocumento dataRes = this.firmaDocumentoRepository.save(data);
        try {
            if (dataRes != null) {
                resp.setEstado(true);
                resp.setMensaje("Datos procesados correctamente.");
                resp.setData(Utils.toJson(dataRes));
            }
        } catch (Exception e) {
            resp.setEstado(false);
            resp.setMensaje(e.getMessage());
        }
        return resp;
    }

    public RespuestaWs actualizarFirmaDocumentosDesk(FirmaDocumentoDto dto) {
        try {
            FirmaDocumento fd = firmaDocumentoRepo.getById(dto.getId());
            FirmaDocumentoDto dtoDb = firmaDocumentoMapper.toDto(fd);
            dto.setTipo(dtoDb.getTipo());
            dto.setEstado(dtoDb.getEstado());
            byte[] decoded = java.util.Base64.getDecoder().decode(dto.getArchivoFirmadoB64());
            String archivoPDF = appProps.getRutaArchivos() + dto.getMotivo() + ".pdf";
            FileOutputStream fos = new FileOutputStream(archivoPDF);
            fos.write(decoded);
            fos.flush();
            fos.close();
            ArchivoIndexDto ai = new ArchivoIndexDto();
            ai.setReferencia(FirmaDocumento.class.getName());
            ai.setReferenciaId(dto.getReferencia());
            ai.setDetalleDocumento(dto.getMotivo());
            ai.setTipoIndexacion("FIRMA_ELECTRONICA_ESCRITORIO");
            ai.setUsuarioDoc(dto.getUsuario());
            String archivo = reporteService.generarReporteDocumentalPDF(decoded, dto.getMotivo(), ai);
            if (Utils.isNotEmptyString(archivo)) {
                dto.setDocumento(archivo);
                return actualizarFirmaDocumentos(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RespuestaWs(Boolean.FALSE, null, Constantes.intenteNuevamente);

    }

    public List<FirmaDocumento> findByEstadoAnTipoFirmaAndreferencia(FirmaDocumento firma) {
        List<FirmaDocumento> list = firmaDocumentoRepository.findByEstado_CodigoAndTipo_CodigoAndReferencia(firma.getEstado().getCodigo(), firma.getTipo().getCodigo(), firma.getReferencia());
        if (Utils.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            return list;
        }
    }

    public Integer countByServidorEstado(Long servidor) {
        return firmaDocumentoRepository.countByServidorAndEstado_IdAndDetalleIsNull(servidor, appProps.getFirmaPendienteId());
    }

    public RespuestaWs inactivarFirmaDocumento(List<FirmaDocumento> data) {
        System.out.println("inactivarFirmaDocumento");
        RespuestaWs rw = null;
        try {
            if (Utils.isNotEmpty(data)) {
                for (FirmaDocumento d : data) {
                    System.out.println("inactivarFirmaDocumento referencia:" + d.getReferencia());
                    d.setEstado(new CatalogoItem(appProps.getFirmaInactivaId()));
                    this.firmaDocumentoRepository.save(d);
                }
            }
            rw = new RespuestaWs(Boolean.TRUE, null, "Eliminados");
        } catch (Exception e) {
            e.printStackTrace();
            rw = new RespuestaWs(Boolean.FALSE, null, e.getMessage());
        }
        return rw;
    }
}
