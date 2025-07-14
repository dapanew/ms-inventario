package com.linktic.ms_inventario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.linktic.ms_inventario.config.FeignConfig;
import com.linktic.ms_inventario.dto.ProductoResponse;

@FeignClient(
    name = "producto-service",
    url = "${producto.service.url}",
    configuration = FeignConfig.class
)
public interface ProductoClient {
    @GetMapping("/api/productos/{id}")
    ProductoResponse obtenerProducto(
        @PathVariable("id") Long id,
        @RequestHeader("X-API-KEY") String apiKey
    );
}