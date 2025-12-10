package org.sas.administrativo.service.documental;

import org.sas.administrativo.conf.AppProps;
import org.sas.administrativo.dto.documental.*;
import org.sas.administrativo.service.commons.RestService;
import org.sas.administrativo.util.ReporteFormato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class DocumentalService {
    private static final Logger logger = Logger.getLogger(DocumentalService.class.getName());
    @Autowired
    private AppProps appProps;
    @Autowired
    private RestService restService;

    /**
     * @param referenciaId    id
     * @param referenciaClazz Proyecto.class.getName()
     * @param detalle         Detalle del documento
     * @param indexTipo       indexTipo
     * @param nombreArchivo   .pdf
     * @param usuarioDocs     usuarioDocs
     * @param reporte         reporte
     * @param campos          Map de los campos con su valor que se pusieron cuando se creo el tipo de indexacion
     * @return return
     */


    public String guardarGestorDocumental(Long referenciaId,
                                          String referenciaClazz, String detalle,
                                          String nombreArchivo, String indexTipo,
                                          UsuarioDocs usuarioDocs, byte[] reporte,
                                          Map<String, String> campos) {
        logger.info("guardarGestorDocumental");
        Indexacion indice = (Indexacion) restService.restGET(appProps.getUrlDocumental() + "indexacion/consultar/" + indexTipo, Indexacion.class);

        ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
        archivoIndex.setReferenciaId(referenciaId);
        archivoIndex.setReferencia(referenciaClazz);
        archivoIndex.setDetalleDocumento(detalle);
        archivoIndex.setEstado(Boolean.TRUE);
        archivoIndex.setTipoIndexacion(indice == null ? indexTipo : indice.getDescripcion());
        archivoIndex.setFormatoUpload(ReporteFormato.PDF.getDescripcion());
        List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
        if (indice != null) {
            for (IndexacionCampo ic : indice.getCampos()) {
                if (campos.containsKey(ic.getDescripcion())) {
                    ic.setDetalle(campos.get(ic.getDescripcion()));
                }
                detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
            }
        }
        archivoIndex.setDetalles(detalles);
        ArchivoDocs archivoDocs = restService.guardarArchivo(usuarioDocs, ReporteFormato.PDF.getCodigo(), nombreArchivo + ReporteFormato.PDF.getExtension(), reporte, archivoIndex);
        if (archivoDocs != null) {
            return archivoDocs.getId();
        }
        return "";

    }

}
