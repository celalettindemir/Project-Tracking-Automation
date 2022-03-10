package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.Paper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface PaperRepository extends CrudRepository<Paper,Long> {
    @Query(value =  "SELECT * FROM paper p WHERE p.project_id = :id",nativeQuery = true)
    List<Paper> findPapersByProjectId(Long id);


}