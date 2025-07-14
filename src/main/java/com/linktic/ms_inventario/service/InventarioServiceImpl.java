package com.linktic.ms_inventario.service;


import com.linktic.ms_inventario.client.ProductoClient;
import com.linktic.ms_inventario.dto.ActualizarInventarioRequest;
import com.linktic.ms_inventario.dto.InventarioResponse;
import com.linktic.ms_inventario.dto.ProductoResponse;
import com.linktic.ms_inventario.entity.Inventario;
import com.linktic.ms_inventario.exception.ProductoServiceException;
import com.linktic.ms_inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoClient productoClient;

    @Override
    @Transactional(readOnly = true)
    public InventarioResponse consultarInventario(Long productoId) {
        // Obtener producto del microservicio de productos, si no existe lanzar excepción que el producto no existe
        try {
            productoClient.obtenerProducto(productoId, "API-KEY-SECRET");
        } catch (Exception e) {
            log.error("Error al consultar el productoen el servicio con ID: {}", productoId, e);
            throw new ProductoServiceException("Producto no encontrado con ID: " + productoId);
        }
        // Obtener producto completo
        // Aquí se asume que el productoClient ya maneja la excepción si el producto no existe
        // Si el producto no existe, se lanzará una excepción que será manejada por el  controller
        // y se retornará un error 404 al cliente.  
        ProductoResponse producto = productoClient.obtenerProducto(productoId, "API-KEY-SECRET");
        
        // Obtener inventario local
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseGet(() -> Inventario.builder()
                        .productoId(productoId)
                        .cantidad(0)
                        .build());

        // Construir respuesta
        return InventarioResponse.builder()
                .id(inventario.getId())
                .productoId(inventario.getProductoId())
                .cantidad(inventario.getCantidad())
                .fechaActualizacion(inventario.getFechaActualizacion())
                .producto(producto)
                .build();
    }

    @Override
    @Transactional
    public InventarioResponse actualizarInventario(Long productoId, ActualizarInventarioRequest request) {
        // Verificar existencia del producto
        try {
            productoClient.obtenerProducto(productoId, "API-KEY-SECRET");
        } catch (Exception e) {
            throw new ProductoServiceException("Error el producto  a consultar no existe ID: " + productoId);
        }

        //validar cantidad que sea mayor o igual a 0, necesito retornar un 400 Bad Request si la cantidad es negativa no un 500 Internal Server Error
        if (request.getCantidad() < 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "La cantidad debe ser mayor o igual a 0"
            );
        }

        // Obtener o crear registro de inventario,
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseGet(() -> Inventario.builder()
                        .productoId(productoId)
                        .cantidad(0)
                        .build());

        // Guardar cantidad anterior para el evento
        int cantidadAnterior = inventario.getCantidad();
        
        // Actualizar cantidad
        inventario.setCantidad(request.getCantidad());
       Inventario updated = inventarioRepository.save(inventario);

        // Emitir evento de cambio
        emitirEventoCambioInventario(productoId, cantidadAnterior, request.getCantidad());

        // Retornar respuesta
        return InventarioResponse.builder()
                .id(updated.getId())
                .productoId(updated.getProductoId())
                .cantidad(updated.getCantidad())
                .fechaActualizacion(updated.getFechaActualizacion())
                .build();
    }

    private void emitirEventoCambioInventario(Long productoId, int cantidadAnterior, int cantidadNueva) {
        log.info("Evento de cambio de inventario - Producto ID: {}, Cantidad anterior: {}, Nueva cantidad: {}",
                productoId, cantidadAnterior, cantidadNueva);
        // Aquí podrías agregar envío a Kafka/RabbitMQ si es necesario
    }
}
