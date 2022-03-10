package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.Privilege;
import com.celal258.cagoz.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

    Privilege findByName(String name);
}
