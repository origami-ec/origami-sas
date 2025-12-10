package org.sas.administrativo.resource;


import org.sas.administrativo.dto.RespuestaWs;
import org.sas.administrativo.entity.configuracion.Catalogo;
import org.sas.administrativo.entity.configuracion.CatalogoItem;
import org.sas.administrativo.service.commons.CatalogoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping(value = "catalogos")
public class CatalogoItemResource {
    @Autowired
    private CatalogoItemService service;


    /**
     * Find all page response entity.
     *
     * @param data     the data
     * @param pageable the pageable
     * @return the response entity
     */
    @GetMapping("/findAll/page")
    public ResponseEntity<List<CatalogoItem>> findAllPage(@Valid CatalogoItem data, Pageable pageable) {
        Page<CatalogoItem> allPage = null;
        try {
            allPage = service.findAllPage(data, pageable);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("rootSize", allPage.getTotalElements() + "");
            headers.add("totalPage", allPage.getTotalPages() + "");
            return new ResponseEntity<>(allPage.getContent(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }


    /**
     * Recibe el codigo de catalogo y devuelve todos los catalogo item relacionado a ese codigo
     */

    @PostMapping(value = "/lista/{codigo}")
    public ResponseEntity<?> findAllByCatalogoCodigo(@PathVariable String codigo) {
        try {
            return new ResponseEntity<>(service.listaItems(codigo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/listarPorCodigo/{codigo}")
    public ResponseEntity<?> findAllByCodigoAndActivo(@PathVariable String codigo) {
        try {
            return new ResponseEntity<>(service.findAllByCodigoAndEstado(codigo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Recibe el id del catalogo item y devuelve el objeto completo
     */

    @PostMapping(value = "/code/{code}")
    public ResponseEntity<?> findById(@PathVariable Long code) {
        try {
            return new ResponseEntity<>(service.findById(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/code/{code}/item/{itemCode}")
    public ResponseEntity<?> findById(@PathVariable String code, @PathVariable String itemCode) {
        try {
//            log.info("code {} {}", code, itemCode);
            return new ResponseEntity<>(service.findByCodigoCatalogoAndcodigoItem(code, itemCode), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/codigo/{code}/item/{itemCode}")
    public ResponseEntity<?> findByIdCatalogoCatalogoItem(@PathVariable String code, @PathVariable String itemCode) {
        try {
            return new ResponseEntity<>(service.findByCodigoCatalogoCatalogotItem(code, itemCode), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/find/codigocatalogo/{codigo}", method = RequestMethod.POST)
    public ResponseEntity<?> findCatalogoItemByCodigoCatalog(@PathVariable String codigo) {
        return new ResponseEntity<>(service.findAllCatalogoItemBycodeCatalogo(codigo), HttpStatus.OK);
    }

    @PostMapping(value = "/find/catalogoItem/codCatalogo/{cod_catalogo}/codItem/{cod_item}")
    public ResponseEntity<?> findCatalogoItemByCodigo(@PathVariable String cod_catalogo, @PathVariable String cod_item) {
        return new ResponseEntity<>(service.findByCodCatalogoAndCodItem(cod_catalogo, cod_item), HttpStatus.OK);
    }

    @PostMapping(value = "/find")
    public ResponseEntity<?> find(@RequestBody CatalogoItem data) {
        try {
            return new ResponseEntity<>(service.findOne(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/find/codigocatalogo/group/{codigo}", method = RequestMethod.POST)
    public ResponseEntity<?> findCatalogoItemByCodigoCatalogGroup(@PathVariable String codigo) {
        return new ResponseEntity<>(service.findAllCatalogoItemBycodeCatalogoGroup(codigo), HttpStatus.OK);
    }

    @PostMapping("/guardar")
    public ResponseEntity<RespuestaWs> register(@RequestBody CatalogoItem data) {
        RespuestaWs resp = new RespuestaWs();
        try {
            resp = service.save(data);
        } catch (Exception e) {

            resp.setEstado(false);
            resp.setMensaje(e.getMessage());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    @PostMapping(value = "/find/codigoItem/{codigoItem}")
    public ResponseEntity<?> findCatalogoItemByCod(@PathVariable String codigoItem) {
        return new ResponseEntity<>(service.findCatalogoItemByCod(codigoItem), HttpStatus.OK);
    }

    @PostMapping("/findAll")
    public ResponseEntity<List<CatalogoItem>> findAll(@RequestBody CatalogoItem data) {
        try {
            return new ResponseEntity<>(service.findAll(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/guardar/catalogo")
    public ResponseEntity<?> guardarCatalogo(@RequestBody Catalogo catalogo) {
        return new ResponseEntity<>(service.guardarCatalogo(catalogo), HttpStatus.OK);
    }


    @PostMapping(value = "/guardar/item")
    public ResponseEntity<?> guardarCatalogoitem(@RequestBody CatalogoItem catalogoItem) {
        return new ResponseEntity<>(service.guardarCatalogoItem(catalogoItem), HttpStatus.OK);
    }


    @PostMapping(value = "/listar/items/{catalogo}")
    public ResponseEntity<?> guardarCatalogo(@PathVariable Long catalogo) {
        return new ResponseEntity<>(service.listaItems(catalogo), HttpStatus.OK);
    }


    @PostMapping(value = "/secuencia")
    public ResponseEntity<?> secuencia(@RequestBody Catalogo catalogo) {
        return new ResponseEntity<>(service.secuencia(catalogo), HttpStatus.OK);
    }


}
