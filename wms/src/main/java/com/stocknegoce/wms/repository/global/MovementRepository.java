package com.stocknegoce.wms.repository.global;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stocknegoce.wms.model.Movement;
import com.stocknegoce.wms.repository.GenericRepository;

public interface MovementRepository extends GenericRepository<Movement, Integer> {

    /**
     * 5 derniers mouvements
     */
    List<Movement> findTop5ByOrderByCreatedAtDesc();

    /**
     * Nombre de mouvements entre deux timestamps
     */
    @Query("SELECT COUNT(m) FROM Movement m WHERE m.createdAt >= :start AND m.createdAt < :end")
    Long countMovementsBetween(@Param("start") Timestamp start, @Param("end") Timestamp end);

    /**
     * Mouvements entre deux timestamps
     */
    @Query("SELECT m FROM Movement m WHERE m.createdAt >= :start AND m.createdAt < :end ORDER BY m.createdAt DESC")
    List<Movement> findMovementsBetween(@Param("start") Timestamp start, @Param("end") Timestamp end);
}