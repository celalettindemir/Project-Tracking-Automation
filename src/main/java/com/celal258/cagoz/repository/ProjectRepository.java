package com.celal258.cagoz.repository;

import com.celal258.cagoz.entity.Paper;
import com.celal258.cagoz.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    @Query(value = "SELECT id,project_description,project_name,project_type,customer_id FROM project",nativeQuery = true)
    String[] findProjects();

    @Query(value =  "SELECT * FROM project p WHERE p.customer_id = :id",nativeQuery = true)
    List<Project> findProjectByCustomerId(Long id);

    @Query(value =  "DELETE FROM project p WHERE p.customer_id = :id",nativeQuery = true)
    List<Project> deleteProjectByCustomerId(Long id);

    @Override
    void deleteById(Long aLong);
}
