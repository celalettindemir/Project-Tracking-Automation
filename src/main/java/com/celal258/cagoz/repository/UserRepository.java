package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
