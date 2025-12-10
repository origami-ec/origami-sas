package org.sas.seguridad.resource;

import org.sas.seguridad.service.BusquedaService;
import org.sas.seguridad.util.Utils;
import org.sas.seguridad.util.model.BusquedaDinamica;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Catalogo mef api.
 */
@RestController()
@RequestMapping("/busquedas")
public class BusquedaApi {
    private static final Logger LOG = Logger.getLogger(BusquedaApi.class.getName());
    private final BusquedaService service;

    /**
     * @param service Servicio donde se encuentra toda la logica de negocia.
     */
    public BusquedaApi(BusquedaService service) {
        this.service = service;
    }


    /**
     * Find by response entity.
     *
     * @param data the data
     * @return the response entity
     */
    @PostMapping(value = "/findBy", produces = "application/json")
    public ResponseEntity<?> findBy(@RequestBody BusquedaDinamica data) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Object result = null;
        try {
            result = this.service.findAllDinamic(data.getEntity(), data);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            headers.add("error", e.getMessage());
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/findBy/page", produces = "application/json")
    public ResponseEntity<?> findByPage(@RequestBody BusquedaDinamica data) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Object result = null;
        try {
            result = this.service.findAllDinamic(data.getEntity(), data, headers);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            headers.add("error", e.getMessage());
            return new ResponseEntity<>(headers, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }


    @GetMapping("/ejemplo")
    public ResponseEntity<?> findAllPage() {
        try {
            BusquedaDinamica b = new BusquedaDinamica();
            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("codigo", "ASC");
            Map<String, Object> filters = new LinkedHashMap<>();
            filters.put("codigo", Utils.toJson(new BusquedaDinamica.WhereCondition(Arrays.asList("99"))));
            b.setEntity("CatalogoMef");
            b.setOrders(fields);
            b.setFilters(filters);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            return new ResponseEntity<>(b, headers, HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/ejemplo/or")
    public ResponseEntity<?> findAllPageOr() {
        try {
            BusquedaDinamica b = new BusquedaDinamica();
            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("codigo", "desc");
            Map<String, Object> filters = new LinkedHashMap<>();
            filters.put("unidadMedida", Utils.toJson(new BusquedaDinamica.WhereCondition("OR", Arrays.asList("METROS", "PESO"))));
            b.setEntity("CatalogoMef");
            b.setOrders(fields);
            b.setFilters(filters);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            return new ResponseEntity<>(b, headers, HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/ejemplo/or/1")
    public ResponseEntity<?> findAllPageOr1() {
        try {
            BusquedaDinamica b = new BusquedaDinamica();
            b.setPageSize(2);
            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("codigo", "desc");
            Map<String, Object> filters = new LinkedHashMap<>();
            Map<String, Object> filtersor = new LinkedHashMap<>();
            filtersor.put("codigo", new BusquedaDinamica.WhereCondition(Arrays.asList("3899739299", new BusquedaDinamica.WhereCondition("startswith", Arrays.asList("21")))));
            filters.put("unidadMedida", Utils.toJson(new BusquedaDinamica.WhereCondition("OR", Arrays.asList("METROS", filtersor))));
            b.setEntity("CatalogoMef");
            b.setOrders(fields);
            b.setFilters(filters);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            return new ResponseEntity<>(b, headers, HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/ejemplo/functions")
    public ResponseEntity<?> findAllFunction() {
        try {
            BusquedaDinamica b = new BusquedaDinamica();
            b.setEntity("Canton");
            b.setPageSize(2);
            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("codigo", "desc");
            Map<String, Object> filters = new LinkedHashMap<>();
            Map<String, Object> functions = new LinkedHashMap<>();
            functions.put("max", "id");
            functions.put("min", "id");
            b.setOrders(fields);
            b.setFilters(filters);
            b.setFunctions(functions);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            return new ResponseEntity<>(b, headers, HttpStatus.OK);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
}
