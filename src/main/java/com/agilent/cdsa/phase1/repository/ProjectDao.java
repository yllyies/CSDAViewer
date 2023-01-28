package com.agilent.cdsa.phase1.repository;

import com.agilent.cdsa.phase1.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProjectDao extends JpaRepository<Project, BigDecimal>, JpaSpecificationExecutor<Project>
{

}
