package com.stocknegoce.wms.repository.global;

import java.util.Optional;

import com.stocknegoce.wms.model.Location;
import com.stocknegoce.wms.repository.GenericRepository;


public interface LocationRepository extends GenericRepository<Location, Integer> {

    Optional<Location> findByQrToken(String qrToken);
}
