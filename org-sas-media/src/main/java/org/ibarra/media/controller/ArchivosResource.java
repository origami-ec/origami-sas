package org.ibarra.media.controller;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.ibarra.media.config.AppProps;
import org.ibarra.media.model.Imagenes;
import org.ibarra.media.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ArchivosResource {

    @Autowired
    private AppProps appProp;
    @Autowired
    private Utils utils;
    private org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
    private static final Logger LOG = Logger.getLogger(ArchivosResource.class.getName());

    @RequestMapping(value = "/resource/image/{ruta}",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String ruta) throws IOException {
        String archivo = new String(base64.decode(ruta.getBytes()));

        // ✅ Ajuste de ruta temporal
        if (archivo.startsWith("imgtemp_") && appProp.getImagenes() != null) {
            archivo = archivo.replace("imgtemp_", appProp.getImagenes());
        }

        File file = new File(archivo);
        System.out.println("Ruta archivo: " + archivo + " existe: " + file.exists());

        if (!file.exists()) {
            return null;
        }

        // ✅ Si es PDF → generar vista previa dinámica
        if (archivo.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(file)) {
                PDFRenderer renderer = new PDFRenderer(document);
                BufferedImage image = renderer.renderImageWithDPI(0, 100); // primera página, 100 DPI
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", baos);
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // ✅ Si es imagen → devolver bytes originales
        try (InputStream targetStream = new FileInputStream(file)) {
            return IOUtils.toByteArray(targetStream);
        }
    }

    /**
     * Se medoifica a que solo devuelva una sola pagina por motivos de que cuando es un documento demasiado grande genera errores
     *
     * @param archivo ruta del archivo a firmar
     * @param pagina  numero de la pagina que se firma
     * @return lista d imagenes del documento en pdf
     */

    @RequestMapping(value = "/resource/pdfImagenes/{archivo}/pagina/{pagina}", method = RequestMethod.GET)
    public List<Imagenes> pdfToImagen(@PathVariable String archivo, @PathVariable Integer pagina) {

//        String pathFile = utils.replaceRutaArchivo(archivo);
//        pagina = 5;
        System.out.println("ruta que llega: " + archivo + " pagina>>" + pagina);
        String pathFile = new String(base64.decode(archivo.getBytes()), StandardCharsets.UTF_8);
        List<Imagenes> files = new ArrayList<>();
        if (pathFile != null) {
            String fileName, tempName;
            BufferedImage bim;
            System.out.println("Archivo a imagenes " + pathFile);

            File file = new File(pathFile);
            String nameFile = "imgtemp_" + file.getName().replace(".pdf", "_");
            fileName = appProp.getImagenes() + file.getName().replace(".pdf", "_");

            String url = "https://smartgob.ibarra.gob.ec/comprasPublicas/imagenCP/imagen/"; // + Base64.getEncoder().encodeToString(nameFile.getBytes());
            System.out.println("url " + url);
            System.out.println("Archivo a imagenes " + file.getAbsolutePath());
            try (final PDDocument document = PDDocument.load(file)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (int page = 0; page < document.getNumberOfPages(); ++page) {
//                    Integer page = pagina - 1;//SE RESTA PORQUE CUANDO SE LEE EMPIEZA DESDE CERO
                    System.out.println("page: " + pagina);
                    PDPage page1 = document.getPage(page);
                    bim = pdfRenderer.renderImageWithDPI(page, 50, ImageType.RGB);
                    bim = utils.resize(bim, Float.valueOf(page1.getMediaBox().getWidth()).intValue(), Float.valueOf(page1.getMediaBox().getHeight()).intValue());
                    tempName = fileName + page + ".png";
                    //files.add(new Imagenes("Pagina # " + page, url + page + ".png", tempName));
                    String base64img = Base64.getEncoder().encodeToString(nameFile.concat(page + ".png").getBytes());
                    files.add(new Imagenes("Pagina # " + page, url + base64img, base64img));
                    ImageIOUtil.writeImage(bim, tempName, 50);
                }
            } catch (IOException e) {
                files.add(new Imagenes("Pagina # 1", appProp.getUrlResource() + "resource/image/sin_resultados.png"));
                e.printStackTrace();
            }
        } else {
            files.add(new Imagenes("Pagina # 1", appProp.getUrlResource() + "resource/image/sin_resultados.png"));
        }
        return files;
    }

    @RequestMapping(value = "/resource/pdfImagenes/{archivo}", method = RequestMethod.GET)
    public ResponseEntity<List<Imagenes>> pdfToImagen(
            @PathVariable String archivo,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1") int size) {
        System.out.println("ruta que llega: " + archivo + " pagina>>" + page + " size>>" + size);
        String pathFile = new String(base64.decode(archivo.getBytes()), StandardCharsets.UTF_8);
        List<Imagenes> files = new ArrayList<>();
        Integer totalPages = 0; // Inicializamos totalPages
        HttpHeaders responseHeader = new HttpHeaders();
        if (pathFile != null) {
            String fileName, tempName;
            BufferedImage bim;
            System.out.println("Archivo a imagenes " + pathFile);
            File file = new File(pathFile);
            String nameFile = "imgtemp_" + file.getName().replace(".pdf", "_");
            fileName = appProp.getImagenes() + file.getName().replace(".pdf", "_");
//            String url = "https://smartgob.ibarra.gob.ec/comprasPublicas/imagenCP/imagen/";
//            System.out.println("url " + url);
            System.out.println("Archivo a imagenes " + file.getAbsolutePath());
            try (final PDDocument document = PDDocument.load(file)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                totalPages = document.getNumberOfPages();
                if (page >= 0 && page < totalPages) {
                    PDPage pageDoc = document.getPage(page);
                    System.out.println("Procesando página: " + page);
                    bim = pdfRenderer.renderImageWithDPI(page, 80, ImageType.RGB);
                    bim = utils.resize(bim, Float.valueOf(pageDoc.getMediaBox().getWidth()).intValue(), Float.valueOf(pageDoc.getMediaBox().getHeight()).intValue());
                    tempName = fileName + page + ".png";
                    String base64img = Base64.getEncoder().encodeToString(nameFile.concat(page + ".png").getBytes());
                    files.add(new Imagenes("Página #" + page, null, base64img));
                    ImageIOUtil.writeImage(bim, tempName, 80);
                } else {
                    files.add(new Imagenes("Página no encontrada", appProp.getUrlResource() + "resource/image/sin_resultados.png"));
                }
            } catch (IOException e) {
                files.add(new Imagenes("Error al procesar el archivo", appProp.getUrlResource() + "resource/image/sin_resultados.png"));
                e.printStackTrace();
            }
        } else {
            files.add(new Imagenes("Archivo no encontrado", appProp.getUrlResource() + "resource/image/sin_resultados.png"));
        }
//        responseHeader.add("result", files);
        responseHeader.add("totalPages", totalPages.toString());
        responseHeader.add("rootSize", totalPages.toString()); // Tamaño total de elementos (el total de páginas en este caso)
        return new ResponseEntity<>(files, responseHeader, HttpStatus.OK); // Devolvemos la respuesta con la lista de imágenes y la paginación como ResponseEntity;
    }

    /**
     * Metodos de verificación para archivos
     *
     * @param archivo ruta = ar_Users-OrigamiEC-Documents-grafi.pdf
     * @return Byte[] pdf
     */

    @RequestMapping(value = "resource/pdf/{archivo}/descarga/{descarga}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFile(@PathVariable String archivo, @PathVariable String descarga) {
        LOG.log(Level.WARNING, "Ruta Original: " + archivo);
//        String filename = utils.replaceRutaArchivo(archivo);
        String filename = new String(base64.decode(archivo.getBytes()));
        LOG.log(Level.WARNING, "Ruta reemplazada: " + filename);
        File file = new File(filename);

        byte[] pdfContents = null;
        try {
            String archivoDescarga = file.getName();
//            String[] nombreArchivo = archivo.split("_");
//            StringBuilder archivoDescarga = new StringBuilder();
//            for (int i = 2; i < nombreArchivo.length; i++) {
//                archivoDescarga.append(" ").append(nombreArchivo[i]);
//            }
            LOG.log(Level.WARNING, "Ruta Final: " + archivo);
            LOG.log(Level.WARNING, "archivoDescarga: " + archivoDescarga);
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", (descarga.equals("DOWNLOAD") ? "attachment" : "inline") + "; filename=" + archivoDescarga);
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "resource/pdf/{archivo}/descarga/{descarga}/nombre/{nombre}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFile(@PathVariable String archivo, @PathVariable String descarga, @PathVariable String nombre) {
        LOG.log(Level.WARNING, "Ruta Original: " + archivo);
//        String filename = utils.replaceRutaArchivo(archivo);
        String filename = new String(base64.decode(archivo.getBytes()));
        LOG.log(Level.WARNING, "Ruta reemplazada: " + archivo);
        File file = new File(filename);

        byte[] pdfContents = null;
        try {
//            String[] nombreArchivo = archivo.split("_");
//            StringBuilder archivoDescarga = new StringBuilder();
//            for (int i = 2; i < nombreArchivo.length; i++) {
//                archivoDescarga.append(" ").append(nombreArchivo[i]);
//            }
            LOG.log(Level.WARNING, "Ruta Final: " + archivo);
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", (descarga.equals("DOWNLOAD") ? "attachment" : "inline") + "; filename=" + nombre);

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "resource/pdf/b4/{archivo}/descarga/{descarga}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFileBase64(@PathVariable String archivo, @PathVariable String descarga) {
        byte[] decoded = java.util.Base64.getDecoder().decode(archivo);
        String filePath = new String(decoded, Charset.forName("UTF-8"));
        System.out.println("File " + filePath);
        HttpHeaders headers = new HttpHeaders();
        File file = new File(filePath);
        byte[] pdfContents = new byte[0];
        if (file.exists()) {
            try {
                String[] nombreArchivo = archivo.split("_");
                StringBuilder archivoDescarga = new StringBuilder();
                for (int i = 2; i < nombreArchivo.length; i++) {
                    archivoDescarga.append(" ").append(nombreArchivo[i]);
                }
                System.out.println("path " + file.getPath());
                pdfContents = Files.readAllBytes(file.toPath());
                headers.add("Content-Disposition", (descarga.equals("DOWNLOAD") ? "attachment" : "inline") + "; filename=" + archivoDescarga);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            pdfContents = createEmptyFile();
        }
        return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
    }

    public byte[] createEmptyFile() {
        try {
            File f = File.createTempFile("pdf", "pdf");
            //Creating PDF document object
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            PDFont font = PDType1Font.COURIER;
            String title = "No se encontro archivo";
            int fontSize = 32; // Or whatever font size you want.
            float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
            float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() / 2);
            contentStream.drawString(title);
            contentStream.endText();
            contentStream.close();
            //Add an empty page to it
            document.addPage(page);
            //Saving the document
            document.save(f.getAbsoluteFile());
            f.deleteOnExit();
            //Closing the document
            document.close();
            return Files.readAllBytes(f.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    @RequestMapping(value = "resource/docs/{archivo}/descarga/{descarga}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<byte[]> downloadOfficeFile(@PathVariable String archivo, @PathVariable String descarga) {
//        String filename = utils.replaceRutaArchivo(archivo);
        String filename = new String(base64.decode(archivo.getBytes()));

        File file = new File(filename);
        byte[] pdfContents = null;
        try {
            String[] nombreArchivo = archivo.split("_");
            StringBuilder archivoDescarga = new StringBuilder();
            for (int i = 2; i < nombreArchivo.length; i++) {
                archivoDescarga.append(" ").append(nombreArchivo[i]);
            }
            System.out.println(archivoDescarga);
            System.out.println(file.getName());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            System.out.println("es descarga" + descarga);
            headers.add("Content-Disposition", (descarga.equals("DOWNLOAD") ? "attachment" : "inline") + "; filename=" + archivoDescarga);

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "resource/xlxs/{archivo}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> downloadXlxFile(@PathVariable String archivo) {
//        String filename = utils.replaceRutaArchivo(archivo);
        String filename = new String(base64.decode(archivo.getBytes()));
        File file = new File(filename);
        byte[] pdfContents = null;
        try {
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            System.out.println(file.getName());
            headers.add("Content-Disposition", "attachment" + "; filename=" + file.getName());
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "resource/pdf/{archivo}", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> downloadPDFFile(@PathVariable String archivo) {
//        String filename = utils.replaceRutaArchivo(archivo);
        String filename = new String(base64.decode(archivo.getBytes()));
        File file = new File(filename);
        byte[] pdfContents = null;
        try {
            String archivoDescarga = file.getName();
//            String[] nombreArchivo = archivo.split("_");
//            StringBuilder archivoDescarga = new StringBuilder();
//            for (int i = 2; i < nombreArchivo.length; i++) {
//                archivoDescarga.append(" ").append(nombreArchivo[i]);
//            }
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline" + "; filename=" + archivoDescarga);

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/resource/descargarImage/{ruta}", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> descargarImage(@PathVariable String ruta) throws IOException {
//        String archivo = utils.replaceRutaArchivo(ruta);
        String archivo = new String(base64.decode(ruta.getBytes()));
        File initialFile = new File(archivo);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] bytes = IOUtils.toByteArray(targetStream);
        targetStream.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment" + "; filename=" + initialFile.getName());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "resource/zip/{archivo}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> downloadZipFile(@PathVariable String archivo) {
        // Decodificamos el archivo desde base64 (si es necesario)
        String filename = new String(base64.decode(archivo.getBytes()));

        File file = new File(filename);
        byte[] fileContents = null;

        try {
            // Comprobamos la ruta del archivo
            System.out.println("path " + file.getPath());

            // Leemos el archivo ZIP
            fileContents = Files.readAllBytes(file.toPath());

            // Configuramos las cabeceras de la respuesta
            HttpHeaders headers = new HttpHeaders();
            System.out.println(file.getName());

            // Aseguramos que el archivo se descargue como un ZIP
            headers.add("Content-Disposition", "attachment; filename=" + file.getName());
            headers.add("Content-Type", "application/zip");  // Tipo MIME para archivos ZIP

            return new ResponseEntity<>(fileContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "resource/dwg/{archivo}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> downloadWgdFile(@PathVariable String archivo) {
        // Decodificamos el archivo desde base64 (si es necesario)
        String filename = new String(base64.decode(archivo.getBytes()));
        File file = new File(filename);
        byte[] fileContents = null;
        try {
            // Comprobamos la ruta del archivo
            System.out.println("path " + file.getPath());
            fileContents = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            System.out.println(file.getName());
            headers.add("Content-Disposition", "attachment; filename=" + file.getName());
            headers.add("Content-Type", "application/acad");  // Tipo MIME para archivos ZIP
            return new ResponseEntity<>(fileContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
