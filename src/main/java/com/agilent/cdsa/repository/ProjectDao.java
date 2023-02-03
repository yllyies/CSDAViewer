package com.agilent.cdsa.repository;

import com.agilent.cdsa.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProjectDao extends JpaRepository<Project, BigDecimal>, JpaSpecificationExecutor<Project>
{

}
