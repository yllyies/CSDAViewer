package com.agilent.csda.acl.repository;

import com.agilent.csda.acl.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project>
{

    List<Project> findByIdIn(List<Integer> ids);
}
