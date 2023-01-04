package com.agilent.cdsa.phase1.repository;

import com.agilent.cdsa.phase1.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project>
{

    List<Project> findByIdIn(List<Integer> ids);
}
