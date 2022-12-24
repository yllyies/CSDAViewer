package com.agilent.csda.phase1.repository;

import com.agilent.csda.phase1.model.Rslt;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RsltDao extends JpaRepository<Rslt, BigDecimal>, JpaSpecificationExecutor<Rslt>
{

    @EntityGraph(value = "Rslt.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Rslt> findAll();

    @EntityGraph(value = "Rslt.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Rslt> findAll(@Nullable Specification<Rslt> var1);

    List<Rslt> findByNodeIdIn(List<Long> nodeIds);

}
