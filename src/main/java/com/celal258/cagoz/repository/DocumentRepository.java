package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.Document;
import com.celal258.cagoz.entity.Paper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, Long> {

    @Query(value =  "SELECT * FROM document p WHERE p.project_id = :id",nativeQuery = true)
    List<Document> findDocumentsByProjectId(Long id);
}
