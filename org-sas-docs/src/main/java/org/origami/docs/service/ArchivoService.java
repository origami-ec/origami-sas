package org.origami.docs.service;


import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import jdk.jshell.execution.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.origami.docs.config.AppProps;
import org.origami.docs.entity.Archivo;
import org.origami.docs.entity.ArchivoIndexacion;
import org.origami.docs.entity.Imagen;
import org.origami.docs.entity.Usuario;
import org.origami.docs.mappers.ArchivoMapper;
import org.origami.docs.mappers.NotaMapper;
import org.origami.docs.model.*;
import org.origami.docs.repository.ArchivoRepository;
import org.origami.docs.util.Constantes;
import org.origami.docs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class ArchivoService {

    @Autowired
    private AppProps appProps;
    @Autowired
    private ArchivoRepository archivoRepository;
    @Autowired
    private ArchivoMapper archivoMapper;
    @Autowired
    private NotaMapper notaMapper;
    @Autowired
    private DocumentoService documentoService;

    public Map<String, List> find(ArchivoDto data, Pageable pageable) {
        System.out.println(data.toString());
        Archivo archivo = archivoMapper.toEntity(data);
        Map map = new HashMap<>();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("numTramite", contains().ignoreCase())
                .withMatcher("tramite", contains().ignoreCase())
                .withMatcher("detalleDocumento", contains().ignoreCase())
                .withMatcher("nombre", contains().ignoreCase());

        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "_id");
        Page<Archivo> result = archivoRepository.findAll(Example.of(archivo, matcher), paging);
        List<ArchivoDto> archivosDto = archivoMapper.toDto(result.getContent());
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(archivoRepository.count(Example.of(archivo, matcher))));
        map.put("result", archivosDto);
        map.put("pages", pages);
        return map;
    }


    public Map<String, List> find2(ArchivoDto data, Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Archivo archivo = archivoMapper.toEntity(data);
        Map map = new HashMap<>();
        Page<Archivo> result = archivoRepository.findAll(Example.of(archivo, exampleMatcher), pageable);
        List<ArchivoDto> archivosDto = archivoMapper.toDto(result.getContent());
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(archivoRepository.count(Example.of(archivo))));
        map.put("result", archivosDto);
        map.put("pages", pages);
        return map;
    }

    public ArchivoDto consultarArchivo(ArchivoDto dto) {
        Archivo archivo = archivoMapper.toEntity(dto);
        Optional<Archivo> archivoBD = archivoRepository.findOne(Example.of(archivo));
        return archivoBD.map(value -> archivoDto(value)).orElse(null);
    }

    public ArchivoDto consultarXid(String id) {
        Archivo archivo = new Archivo(id);
        Optional<Archivo> archivoBD = archivoRepository.findOne(Example.of(archivo));
        return archivoBD.map(value -> archivoDto(value)).orElse(null);
    }

    public List<ArchivoDto> consultarArchivos(ArchivoDto dto) {
        try {
            Archivo archivo = archivoMapper.toEntity(dto);
            List<Archivo> list = archivoRepository.findAll(Example.of(archivo));
            if (Utils.isNotEmpty(list)) {
                for (Archivo a : list) {
                    archivoDto(a);
                }
                return archivoMapper.toDto(list);
            }
            return new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ArchivoDto> consultarXreferencia(ArchivoDto dto) {
        Archivo archivo = archivoMapper.toEntity(dto);
        List<Archivo> archivoBD = archivoRepository.findAll(Example.of(archivo));
        if (Utils.isNotEmpty(archivoBD))
            return archivoMapper.toDto(archivoBD);
        else return new ArrayList<>();
    }

    public ArchivoDto eliminarArchivo(ArchivoDto dto) {
        Archivo archivo = archivoMapper.toEntity(dto);
        Optional<Archivo> archivoBD = archivoRepository.findOne(Example.of(archivo));
        archivoBD.ifPresent(value -> archivoRepository.delete(value));
        return dto;
    }

    /**
     * @param dto recibe un dto lleno pro solo se usan los campos de la descripcion del documento y los campos de la indeacion
     *            porque para eso es este metodo
     * @return el mismo dto xd
     */

    public ArchivoDto actualizarDatosIndex(ArchivoDto dto) {

        Optional<Archivo> archivoBD = archivoRepository.findById(dto.getId());
        if (archivoBD.isPresent()) {
            Archivo arc = archivoBD.get();
            arc.setDetalleDocumento(dto.getDetalleDocumento());
            arc.setDetalles(dto.getDetalles());
            archivoRepository.save(arc);
        }
        return dto;
    }

    public Data agregarNota(ImagenNotaDto dto) {
        try {
            System.out.println(dto.toString());
            ;
            Optional<Archivo> archivo = archivoRepository.findById(dto.getArchivoId());
            if (archivo.isPresent()) {
                Archivo arc = archivo.get();
                Imagen imagen = arc.getImagenes().get(dto.getIndiceImagen());
                if (dto.getTipo().equals(Constantes.notaAgregar))
                    imagen.getNotas().add(notaMapper.toEntity(dto.getNota()));
                else
                    imagen.getNotas().remove(dto.getIndiceNota().intValue());
                arc.getImagenes().set(dto.getIndiceImagen(), imagen);
                arc = archivoRepository.save(arc);
                return new Data(0L, new Gson().toJson(archivoDto(arc)), "Datos grabados correctamente");
            } else {
                return new Data(1L, "", "No se encontraron datos del archivo");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Data(1L, "", "No se encontraron datos del archivo y de la página");
        }


    }

    public byte[] consultarImagenes(String imagen) throws IOException {
        try {
            if (!imagen.endsWith(".png")) {
                imagen = imagen + ".png";
            }
            File file = new File(imagen);
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArchivoDto cargarDocumento(String formato, String usuario, String indexacion, MultipartFile document, Boolean generarImagenes) {
        try {
            if (generarImagenes == null) {
                generarImagenes = false;
            }
            Gson gson = new Gson();
            String archivoFecha = generarNombreArchivo(document.getOriginalFilename());
            String directorioArcvivos = appProps.getRutaArchivo();
            Date fa = new Date();
            if (directorioArcvivos.endsWith("/")) {
                directorioArcvivos = directorioArcvivos + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
            } else {
                directorioArcvivos = directorioArcvivos + "/" + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
            }
            Utils.crearDirectorio(directorioArcvivos);
            String archivo = directorioArcvivos + archivoFecha;
            ArchivoIndexDto archivoIndex = gson.fromJson(indexacion, ArchivoIndexDto.class);
            List<ArchivoIndexacion> detalle = new ArrayList<>();
            if (Utils.isNotEmpty(archivoIndex.getDetalles())) {
                for (ArchivoIndexCampoDto campo : archivoIndex.getDetalles()) {
                    detalle.add(new ArchivoIndexacion(campo.getTipoDato(), campo.getDescripcion(), campo.getCategorias(), campo.getDetalle(), campo.getObligatorio()));
                }
            }

            File f = new File(archivo);
            f.setReadable(true, false);
            f.setExecutable(true, false);
            f.setWritable(true, false);
            FileUtils.writeByteArrayToFile(f, document.getBytes());
            if (archivoIndex.getFormatoUpload() == null) {
                archivoIndex.setFormatoUpload(formato);
            }
            String tipoFormato = Utils.verificarArchivo(f, archivoIndex.getFormatoUpload());


            Archivo arc = new Archivo();
            arc.setNombre(archivoFecha);
            arc.setPeso(FileUtils.byteCountToDisplaySize(document.getSize()));
            arc.setUsuario(gson.fromJson(usuario, Usuario.class));
            arc.setTipo(Constantes.tipoArchivo);
            arc.setFecha(Utils.getDate(new Date()));
            arc.setRuta(archivo);
            arc.setReferencia(archivoIndex.getReferencia());
            arc.setReferenciaId(archivoIndex.getReferenciaId());
            arc.setTramite(archivoIndex.getTramite());
            arc.setNumTramite(archivoIndex.getNumTramite());
            arc.setFormato(tipoFormato);
            arc.setTipoContenido(document.getContentType());
            arc.setFechaCreacion(Utils.getFechaMongo());

            arc.setProceso(archivoIndex.getProceso());
            arc.setSubproceso(archivoIndex.getSubproceso());
            arc.setListadoMaestro(archivoIndex.getListadoMaestro());

            arc.setTipoIndexacion(archivoIndex.getTipoIndexacion());
            arc.setDetalleDocumento(archivoIndex.getDetalleDocumento());
            arc.setEstado(archivoIndex.getEstado());
            arc.setDetalles(detalle);
            arc.setNumeroPaginas(numeroPaginas(archivo, tipoFormato));
            System.out.println("Genera imagenes " + generarImagenes);
            if (Boolean.TRUE.equals(generarImagenes)) {
                convertirPdfImagen(arc, archivo, tipoFormato);
            }

            arc = archivoRepository.save(arc);
            return archivoDto(arc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ArchivoDto cargarDocumentoAspirantes(String usuario, String nombreDocumento, String indexacion, MultipartFile document, Boolean generarImagenes) {
        try {
            if (generarImagenes == null) {
                generarImagenes = false;
            }
            Gson gson = new Gson();
            String archivoFecha = generarNombreArchivo(nombreDocumento);
            String directorioArcvivos = appProps.getRutaArchivo();
            Date fa = new Date();
            if (directorioArcvivos.endsWith("/")) {
                directorioArcvivos = directorioArcvivos + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
            } else {
                directorioArcvivos = directorioArcvivos + "/" + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
            }
            Utils.crearDirectorio(directorioArcvivos);
            String archivo = directorioArcvivos + archivoFecha;

            ArchivoIndexDto archivoIndex = gson.fromJson(indexacion, ArchivoIndexDto.class);
            List<ArchivoIndexacion> detalle = new ArrayList<>();
            if (Utils.isNotEmpty(archivoIndex.getDetalles())) {
                for (ArchivoIndexCampoDto campo : archivoIndex.getDetalles()) {
                    detalle.add(new ArchivoIndexacion(campo.getTipoDato(), campo.getDescripcion(), campo.getCategorias(), campo.getDetalle(), campo.getObligatorio()));
                }
            }
            System.out.println("archivoFecha  " + archivoFecha);
            System.out.println("archivo  " + archivo);

            File f = new File(archivo);
            FileUtils.writeByteArrayToFile(f, document.getBytes());
            String tipoFormato = Utils.verificarArchivo(f, archivoIndex.getFormatoUpload());


            Archivo arc = new Archivo();
            arc.setNombre(archivoFecha);
            arc.setPeso(FileUtils.byteCountToDisplaySize(document.getSize()));
            arc.setUsuario(gson.fromJson(usuario, Usuario.class));
            arc.setTipo(Constantes.tipoArchivo);
            arc.setFecha(Utils.getDate(new Date()));
            arc.setRuta(archivo);
            arc.setReferencia(archivoIndex.getReferencia());
            arc.setReferenciaId(archivoIndex.getReferenciaId());
            arc.setTramite(archivoIndex.getTramite());
            arc.setNumTramite(archivoIndex.getNumTramite());
            arc.setFormato(tipoFormato);
            arc.setTipoContenido(document.getContentType());
            arc.setFechaCreacion(Utils.getFechaMongo());

            arc.setTipoIndexacion(archivoIndex.getTipoIndexacion());
            arc.setDetalleDocumento(archivoIndex.getDetalleDocumento());
            arc.setEstado(archivoIndex.getEstado());
            arc.setDetalles(detalle);
            arc.setNumeroPaginas(numeroPaginas(archivo, tipoFormato));
            arc = archivoRepository.save(arc);
            System.out.println("Genera imagenes " + generarImagenes);
            if (Boolean.TRUE.equals(generarImagenes)) {
                convertirPdfImagen(arc, archivo, tipoFormato);
            }

            return archivoDto(arc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ArchivoDto imprimirArchivo(ArchivoDto dto) {

        return documentoService.imprimirArchivo(dto);
    }

    public Integer numeroPaginas(String archivo, String tipoFormato) {
        switch (tipoFormato) {
            case "PDF":
                try {
                    PDDocument document = PDDocument.load(new File(archivo));
                    return document.getNumberOfPages();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
            case "IMG":
                return 1;
            default:
                return 1;
        }
    }

    @Async
    public void convertirPdfImagen(Archivo arc, String archivo, String tipoFormato) {

        List<Imagen> listImg = new ArrayList<>();
        String archivoImagen, archivoImagenTemp;
        File file;
        String nameFile;
        switch (tipoFormato) {
            case "PDF":
                BufferedImage bim;
                file = new File(archivo);
                nameFile = file.getName().replace(".pdf", "_");
                archivoImagen = appProps.getRutaImagen();

                Date fa = new Date();
                if (archivoImagen.endsWith("/")) {
                    archivoImagen = archivoImagen + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
                } else {
                    archivoImagen = archivoImagen + "/" + Utils.getAnio(fa) + "/" + Utils.getMes(fa) + "/" + Utils.getDia(fa) + "/";
                }
                Utils.crearDirectorio(archivoImagen);
                archivoImagen = archivoImagen + nameFile;
                try (final PDDocument document = PDDocument.load(file)) {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    for (int pagina = 0; pagina < document.getNumberOfPages(); ++pagina) {
                        System.out.println(Constantes.pagNombreImagen + ": " + pagina);
                        bim = pdfRenderer.renderImageWithDPI(pagina, 200, ImageType.RGB);
                        int pag = pagina + 1;
                        archivoImagenTemp = archivoImagen + pag + Constantes.extImagen;
                        listImg.add(new Imagen(pagina, Constantes.pagNombreImagen + pag, nameFile + pag + Constantes.extImagen, archivoImagenTemp));
                        ImageIO.write(bim, "PNG", new File(archivoImagenTemp));
                    }
                } catch (IOException e) {
                    listImg.add(new Imagen(0, "Pagina # 1", Constantes.sinResultados, appProps.getRutaImagen() + Constantes.sinResultados));
                    e.printStackTrace();
                }
                break;
            case "IMG":
                try {
                    file = new File(archivo);
                    String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
                    archivoImagen = appProps.getRutaImagen() + fileNameWithOutExt + Constantes.extImagen;
                    File newfile = new File(archivoImagen);
                    FileUtils.copyFile(file, newfile);
                    listImg.add(new Imagen(0, Constantes.pagNombreImagen + 1, fileNameWithOutExt + Constantes.extImagen, archivoImagen));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
        arc.setImagenes(listImg);


    }


    private ArchivoDto archivoDto(Archivo archivo) {
        ArchivoDto dto = archivoMapper.toDto(archivo);
        if (Utils.isNotEmpty(dto.getImagenes()))
            for (ImagenDto i : dto.getImagenes()) {
                i.setApiUrl(appProps.getApiContext() + appProps.getApiImagenes() + Utils.getFilterRuta(i.getRuta()));
            }
        return dto;
    }

    public String generarNombreArchivo(String nombreArchivo) {
        Date hoy = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String fecha = format.format(hoy);
        nombreArchivo = (fecha + "_" + nombreArchivo).replace(" ", "_").replace(":", "_");
        return nombreArchivo;
    }

    public List<Data> obtenerSellos() {
        List<Data> list = new ArrayList<>();
        List<Archivo> sellos = archivoRepository.findAllByTipoIndexacion("Sellos");
        for (Archivo a : sellos) {
            ArchivoDto d = archivoDto(a);
            list.add(new Data(d.getImagenes().get(0).getApiUrl(), d.getDetalleDocumento()));
        }
        return list;
    }

    public List<ArchivoDto> busquedaAvanzada(ArchivoIndexDto index) {
        System.out.println("busquedaAvanzada");
        List<Archivo> list = new ArrayList<>();
        List<ArchivoDto> dtolist = new ArrayList<>();
        try {
            String textDocumento = index.getTextoDocumento() == null ? null : index.getTextoDocumento().isEmpty() ? null : index.getTextoDocumento();
            if (textDocumento != null) {
                List<Archivo> temp = new ArrayList<>();
                System.out.println("11mo.- textDocumento: " + textDocumento);
                List<Archivo> archivos = archivoRepository.findAll();
                temp = new ArrayList<>();
                for (Archivo c : archivos) {
                    System.out.println(c.getRuta());
                    if (c.getRuta().endsWith(".pdf")) {
                        File f = new File(c.getRuta());
                        if (f.exists()) {
                            try {
                                Boolean existeSubwords = new SearchSubword().exitsSubwords(PDDocument.load(f), textDocumento.toLowerCase());
                                if (existeSubwords) {
                                    temp.add(c);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                continue;
                            }

                        }
                    }
                }
                for (Archivo a : temp) {
                    if (!list.contains(a)) {
                        list.add(a);
                    }
                }
            }
            for (ArchivoIndexCampoDto field : index.getDetalles()) {
                String det = field.getDetalle();
                List<Archivo> temp = new ArrayList<>();

                String numTramite = index.getNumTramite() == null ? null : index.getNumTramite().isEmpty() ? null : index.getNumTramite();
                String tipoIndexacion = index.getTipoIndexacion() == null ? null : index.getTipoIndexacion().isEmpty() ? null : index.getTipoIndexacion();
                String detDocumento = index.getDetalleDocumento() == null ? null : index.getDetalleDocumento().isEmpty() ? null : index.getDetalleDocumento();
                String detalleIndex = field.getDetalle() == null ? null : field.getDetalle().isEmpty() ? null : field.getDetalle();

                if (numTramite != null && tipoIndexacion != null && detDocumento != null && detalleIndex != null) {
                    System.out.println("1er.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion, detDocumento, detalleIndex);
                } else if (numTramite != null && tipoIndexacion != null && detDocumento != null && detalleIndex == null) {
                    System.out.println("2do.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion, detDocumento);
                } else if (numTramite != null && tipoIndexacion != null && detDocumento == null && detalleIndex == null) {
                    System.out.println("3er.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion);
                } else if (numTramite != null && tipoIndexacion == null && detDocumento == null && detalleIndex == null) {
                    System.out.println("4to.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByIndexacion(numTramite);
                } else if (numTramite == null && tipoIndexacion != null && detDocumento == null && detalleIndex == null) {
                    System.out.println("5to.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByTipoIndexacion(tipoIndexacion);
                } else if (numTramite == null && tipoIndexacion != null && detDocumento != null && detalleIndex == null) {
                    System.out.println("6to.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByTipoIndexacion(tipoIndexacion, detDocumento);
                } else if (numTramite == null && tipoIndexacion != null && detDocumento != null && detalleIndex != null) {
                    System.out.println("7mo.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByTipoIndexacion(tipoIndexacion, detDocumento, detalleIndex);
                } else if (numTramite == null && tipoIndexacion != null && detDocumento == null && detalleIndex != null) {
                    System.out.println("8vo.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByTipoIndexacionDets(tipoIndexacion, detalleIndex);
                } else if (numTramite != null && tipoIndexacion != null && detDocumento == null && detalleIndex != null) {
                    System.out.println("9no.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByTramiteTipoIndexacionDets(numTramite, tipoIndexacion, detalleIndex);
                } else {
                    System.out.println("10mo.- numTramite: " + numTramite + "; tipoIndexacion: " + tipoIndexacion + "; detDocumento: " + detDocumento + "; detalleIndex: " + detalleIndex);
                    temp = archivoRepository.busquedaByIndexacion(numTramite, tipoIndexacion, detDocumento, detalleIndex);
                }
                for (Archivo a : temp) {
                    if (!list.contains(a)) {
                        list.add(a);
                    }
                }
            }
            if (Utils.isNotEmpty(list)) {
                for (Archivo a : list) {
                    dtolist.add(archivoDto(a));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtolist;
    }


    private static void appendBody(CTBody src, CTBody append) {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);
        String srcString = src.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String suffix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        CTBody makeBody;
        try {
            makeBody = (CTBody) CTBody.Factory.parse(prefix + mainPart + addPart + suffix);
            src.set(makeBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String convertFileToString(File archivo) {
        String contentType = "";
        Path path = archivo.toPath();
        contentType = path.toString();
        return contentType;
    }

    public byte[] convertFileToBytes(File file) {
        byte[] bytesArray = null;
        try {
            bytesArray = Files.readAllBytes(file.toPath()); // Lee el contenido del archivo en el arreglo de bytes
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }

    public Boolean reemplazarArchivo(ArchivoDto archivoDto) {
        Archivo archivo = archivoRepository.findById(archivoDto.getId()).get();
        String docOriginal = Utils.convertirArchivoABase64(archivo.getRuta());
        if (docOriginal.equals(archivoDto.getDocumentoBs64())) {
            return Boolean.FALSE;
        }
        String directorioArcvivos = appProps.getRutaArchivo();
        try {
            byte[] pdfData = Base64.getDecoder().decode(archivoDto.getDocumentoBs64());
            File file = new File(archivo.getRuta());
            boolean deleted = FileUtils.deleteQuietly(file);
            if (deleted) {
                File fileNew = new File(archivo.getRuta());
                FileUtils.writeByteArrayToFile(fileNew, pdfData);
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }
}
