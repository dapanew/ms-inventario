package com.linktic.ms_inventario.controller;

import com.linktic.ms_inventario.dto.ActualizarInventarioRequest;
import com.linktic.ms_inventario.dto.InventarioResponse;
import com.linktic.ms_inventario.service.InventarioService;



import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.logstash.logback.argument.StructuredArguments.value;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;
     private static final Logger log = LoggerFactory.getLogger(InventarioController.class);


    @GetMapping("/{productoId}")
    public ResponseEntity<InventarioResponse> consultarInventario(@PathVariable Long productoId) {

        log.info("Consultando inventario para producto ID: {}", value("productoId", productoId));
        //crea logica para que si el producto no existe, responda un error 404 diciendo "Producto no encontrado" no como actualmente lo hace que es un error 500
        // Verificar si el productoId es válido 
        if (productoId == null || productoId <= 0) {
            log.error("Producto ID inválido:", value("productoId", productoId));
            return ResponseEntity.notFound().build();
        }
        // Consultar inventario

        InventarioResponse inventarioResponse = inventarioService.consultarInventario(productoId);  
        if (inventarioResponse == null) {
            log.error("Inventario no encontrado para producto ID: {}", value("productoId", productoId));
            return ResponseEntity.notFound().build();
        }
// Log successful retrieval
        log.info("Inventario consultado exitosamente para producto ID: {}"+productoId);
        return ResponseEntity.ok(inventarioService.consultarInventario(productoId));
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<InventarioResponse> actualizarInventario(
            @PathVariable Long productoId,
            @Validated @RequestBody ActualizarInventarioRequest request) {
        return ResponseEntity.ok(inventarioService.actualizarInventario(productoId, request));
    }
}
