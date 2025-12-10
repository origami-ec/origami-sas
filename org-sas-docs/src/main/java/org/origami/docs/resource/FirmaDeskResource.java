package org.origami.docs.resource;

import org.origami.docs.model.ArchivoDto;
import org.origami.docs.service.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class FirmaDeskResource {

    @Autowired
    private ArchivoService service;

    @GetMapping(value = "generarPDF/{archivo}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, consumes = MediaType.ALL_VALUE)

    public @ResponseBody
    byte[] downloadPDFFile(@PathVariable String archivo) {
        ArchivoDto response = service.consultarXid(archivo);

        File file = new File(response.getRuta());
        byte[] pdfContents = null;
        try {
            String[] nombreArchivo = archivo.split("_");
            StringBuilder archivoDescarga = new StringBuilder();
            for (int i = 2; i < nombreArchivo.length; i++) {
                archivoDescarga.append(" ").append(nombreArchivo[i]);
            }
            System.out.println("path " + file.getPath());
            pdfContents = Files.readAllBytes(file.toPath());
            //HttpHeaders headers = new HttpHeaders();
            //headers.add("Content-Disposition", "inline" + "; filename=" + archivoDescarga);

            return pdfContents;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
