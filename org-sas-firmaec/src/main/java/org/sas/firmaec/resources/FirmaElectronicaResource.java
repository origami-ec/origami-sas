package org.sas.firmaec.resources;


import com.google.gson.Gson;
import org.sas.firmaec.rubrica.sign.FirmaElectronicaService;
import org.sas.firmaec.model.FirmaDocDesk;
import org.sas.firmaec.model.FirmaDocumento;
import org.sas.firmaec.model.FirmaDocumentoDB;
import org.sas.firmaec.model.FirmaElectronica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FirmaElectronicaResource {

    @Autowired
    private FirmaElectronicaService service;

    @RequestMapping(value = "firmaElectronica/documentosXservidor/{servidor}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> consultarDocumentos(@PathVariable Long servidor, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            return new ResponseEntity<>(service.consultarDocumentos(servidor, authorizationHeader), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/consultarTokens", method = RequestMethod.GET)
    public ResponseEntity<?> consultarTokens() {
        try {
            return new ResponseEntity<>(service.getAllTokens(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/firmarDesk", method = RequestMethod.POST)
    public ResponseEntity<?> firmarToken(@RequestBody FirmaDocDesk firmaDocDesk) {
        try {
            FirmaElectronica fe = service.firmarDocumentoDesk(firmaDocDesk);
            fe.setArchivoDesk(null);
            return new ResponseEntity<>(fe, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/generar", method = RequestMethod.POST)
    public ResponseEntity<?> generar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
       //     System.out.println(new Gson().toJson(firmaElectronica));
            if (firmaElectronica.getEsToken() == null) {
                firmaElectronica.setEsToken(Boolean.TRUE);
            }
            FirmaElectronica fe = !firmaElectronica.getEsToken() ?
                    service.firmarDocumentoArchivo(firmaElectronica, Boolean.FALSE) :
                    service.firmarDocumentoToken(firmaElectronica);

            return new ResponseEntity<>(fe, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/validar", method = RequestMethod.POST)
    public ResponseEntity<?> validar(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            System.out.println("validarCertificado");
            return new ResponseEntity<>(service.validarCertificado(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            firmaElectronica.setUid(null);
            firmaElectronica.setEstadoFirma(e.getMessage());
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "firmaElectronica/verificarDocumento", method = RequestMethod.POST)
    public ResponseEntity<?> verificarDocumento(@RequestBody FirmaElectronica firmaElectronica) {
        try {

            return new ResponseEntity<>(service.verificarDocumento(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        }
    }


    /*
        Metodo que genera en maquina local el documento
    */
    @RequestMapping(value = "firmaElectronica/generarDocumentoLocal", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public ResponseEntity<?> generarDocumento(@RequestBody FirmaDocumentoDB FirmaDocumento) {
        try {
            FirmaDocumentoDB firmaElectronica = service.generarDocumentoLocal(FirmaDocumento);
            firmaElectronica.setArchivoFirmadoB64(null);
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }


    /*
    Metodo que genera en maquina local el documento
     */
    @RequestMapping(value = "firmaElectronica/subirDocumento", method = RequestMethod.POST)
    public ResponseEntity<?> subirDocumento(@RequestBody FirmaDocumento FirmaDocumento) {
        try {
            FirmaDocumento firmaElectronica = service.subirDocumento(FirmaDocumento);
            firmaElectronica.setArchivoFirmado(null);
            return new ResponseEntity<>(firmaElectronica, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }

    @RequestMapping(value = "firmaElectronica/cargar", method = RequestMethod.POST)
    public ResponseEntity<?> consultarDocumentos(@RequestBody FirmaElectronica firmaElectronica) {
        try {
            return new ResponseEntity<>(service.uploadFirma(firmaElectronica), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
