package com.stocknegoce.wms.repository.global;

import java.util.Optional;

import com.stocknegoce.wms.model.User;
import com.stocknegoce.wms.repository.GenericRepository;

public interface UserRepository extends GenericRepository<User, Integer> {

    Optional<User> findByLogin(String login);
}
