package org.origami.docs.resource;

import org.origami.docs.config.AppProps;
import org.origami.docs.dto.QRCodeRequest;
import org.origami.docs.service.QRCodeService;
import org.origami.docs.util.Constantes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/qr")
public class QRCodeResource {
    @Autowired
    private AppProps appProps;
    @Autowired
    private QRCodeService qrCodeService;


    /**
     * Endpoint para generar un código QR basado en los parámetros del DTO.
     *
     * @param request Objeto QRCodeRequest con los parámetros necesarios.
     * @return Imagen PNG del código QR con texto.
     */
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateQRCode(@RequestBody QRCodeRequest request) {
        try {
            byte[] qrImage = qrCodeService.generateQRCodeWithText(request);


            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/png");

            return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el código QR: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/codigoVerificacionQR-{text}")
    public ResponseEntity<byte[]> generateQRCodeBasic(@PathVariable String text) {
        try {

            byte[] qrImage = qrCodeService.generateQRCodeWithText(
                    new QRCodeRequest(appProps.getUrlDominioCodigoValidacion() + text, Constantes.codigoVerificacion, text)
            );

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/png");

            return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el código QR: " + e.getMessage()).getBytes());
        }
    }

}
