package org.origami.docs.resource;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.origami.docs.config.AppProps;
import org.origami.docs.model.ArchivoDto;
import org.origami.docs.model.ArchivoIndexDto;
import org.origami.docs.model.Data;
import org.origami.docs.model.ImagenNotaDto;
import org.origami.docs.service.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Controller
public class ImagenResource {
    @Autowired
    private AppProps appProps;
    @Autowired
    private ArchivoService service;

    private Base64 base64 = new Base64();

    @RequestMapping(value = "imagens/{imagen}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String imagen) throws IOException {
        System.out.println("getImage: " + imagen);
        byte[] response = service.consultarImagenes(new String(base64.decode(imagen), StandardCharsets.UTF_8));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(response, headers, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "imagenss/{imagen}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE, consumes = MediaType.ALL_VALUE)
    @ResponseBody
    public byte[] getImages(@PathVariable String imagen) {
        try {
            System.out.println("getImage: " + imagen);
            byte[] response = service.consultarImagenes(new String(base64.decode(imagen), StandardCharsets.UTF_8));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment" + "; filename=Documento.png");
            headers.add("Content-Type", MediaType.IMAGE_JPEG_VALUE);
            //return new ResponseEntity<>(response, headers, response != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "imagen/{imagen}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getImageWithMediaType(@PathVariable String imagen) throws IOException {
        try {
            byte[] response = service.consultarImagenes(new String(base64.decode(imagen), StandardCharsets.UTF_8));
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity(response, headers, HttpStatus.OK);

            //return IOUtils.toByteArray(targetStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
