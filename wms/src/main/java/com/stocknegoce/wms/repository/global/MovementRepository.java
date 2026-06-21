package com.stocknegoce.wms.repository.global;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stocknegoce.wms.model.Movement;
import com.stocknegoce.wms.repository.GenericRepository;

public interface MovementRepository extends GenericRepository<Movement, Integer> {

    List<Movement> findTop5ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(m) FROM Movement m WHERE m.createdAt >= :start AND m.createdAt < :end")
    Long countMovementsBetween(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query("SELECT m FROM Movement m WHERE m.createdAt >= :start AND m.createdAt < :end ORDER BY m.createdAt DESC")
    List<Movement> findMovementsBetween(@Param("start") Timestamp start, @Param("end") Timestamp end);

    /**
     * Historique complet trié par date desc
     */
    List<Movement> findAllByOrderByCreatedAtDesc();

    /**
     * Historique par article
     */
    @Query("SELECT m FROM Movement m WHERE m.stockLine.item.id_item = :itemId ORDER BY m.createdAt DESC")
    List<Movement> findByItemId(@Param("itemId") Integer itemId);

    /**
     * Historique par emplacement
     */
    @Query("SELECT m FROM Movement m WHERE m.stockLine.location.id_location = :locationId ORDER BY m.createdAt DESC")
    List<Movement> findByLocationId(@Param("locationId") Integer locationId);

    /**
     * Historique par type
     */
    List<Movement> findByTypeOrderByCreatedAtDesc(String type);

    /**
     * Historique par client
     */
    List<Movement> findByClientNameContainingIgnoreCaseOrderByCreatedAtDesc(String clientName);
}