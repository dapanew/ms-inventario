package com.linktic.ms_inventario.service;

import com.linktic.ms_inventario.dto.ActualizarInventarioRequest;
import com.linktic.ms_inventario.dto.InventarioResponse;
import com.linktic.ms_inventario.dto.ProductoResponse;

public interface InventarioService {
    InventarioResponse consultarInventario(Long productoId);
    InventarioResponse actualizarInventario(Long productoId, ActualizarInventarioRequest request);
}

