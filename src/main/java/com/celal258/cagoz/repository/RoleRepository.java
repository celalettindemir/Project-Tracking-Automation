package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.Role;
import com.celal258.cagoz.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
