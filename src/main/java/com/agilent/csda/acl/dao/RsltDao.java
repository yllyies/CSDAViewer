package com.agilent.csda.acl.dao;

import com.agilent.csda.acl.model.Rslt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RsltDao extends JpaRepository<Rslt, BigDecimal>
{

    @EntityGraph(value = "Rslt.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Rslt> findAll();
}
