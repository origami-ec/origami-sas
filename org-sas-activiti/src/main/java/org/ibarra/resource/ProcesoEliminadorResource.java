package org.ibarra.resource;

import org.ibarra.service.ProcesoEliminadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procesos")
public class ProcesoEliminadorResource {

    @Autowired
    private ProcesoEliminadorService eliminadorService;

    @PostMapping("/eliminar")
    public ResponseEntity<?> eliminar(@RequestBody List<String> processInstanceIds) {
   eliminadorService.eliminarProcesos(processInstanceIds);
        return ResponseEntity.ok().build();
    }

}
