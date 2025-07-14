package com.linktic.ms_inventario.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioResponse {
    private Long id;
    private Long productoId;
    private Integer cantidad;
    private LocalDateTime fechaActualizacion;
    private ProductoResponse producto;
}