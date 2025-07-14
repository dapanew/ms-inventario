package com.linktic.ms_inventario.repository;

import com.linktic.ms_inventario.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    /**
     * Busca un inventario por el ID del producto
     * @param productoId ID del producto
     * @return Optional con el inventario encontrado
     */
    Optional<Inventario> findByProductoId(Long productoId);

    /**
     * Verifica si existe un inventario para un producto específico
     * @param productoId ID del producto
     * @return true si existe, false si no
     */
    boolean existsByProductoId(Long productoId);

    /**
     * Actualiza la cantidad de un producto en el inventario
     * @param productoId ID del producto
     * @param cantidad Nueva cantidad
     * @return Número de registros actualizados
     */
    @Modifying
    @Query("UPDATE Inventario i SET i.cantidad = :cantidad WHERE i.productoId = :productoId")
    int actualizarCantidad(Long productoId, Integer cantidad);

    /**
     * Elimina un registro de inventario por ID de producto
     * @param productoId ID del producto
     */
    @Modifying
    @Query("DELETE FROM Inventario i WHERE i.productoId = :productoId")
    void deleteByProductoId(Long productoId);
}