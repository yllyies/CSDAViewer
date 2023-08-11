package com.agilent.iad.repository;

import com.agilent.iad.dto.RsltVo;
import com.agilent.iad.model.Rslt;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface RsltDao extends JpaRepository<Rslt, BigDecimal>, JpaSpecificationExecutor<Rslt>
{

    @EntityGraph(value = "Rslt.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Rslt> findAll();

    @EntityGraph(value = "Rslt.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Rslt> findAll(@Nullable Specification<Rslt> var1);

    List<Rslt> findByNodeIdIn(List<Long> nodeIds);

    @Query(value = "select new com.agilent.iad.dto.RsltVo(rslt.instrumentName, project.name, rslt.creator, rslt.createdDate) " +
            "from Rslt rslt left join Project project on rslt.projectId = project.id")
    List<RsltVo> findMenuInfo();

    /**
     * 根据时间区间，查询所有是序列的结果集，
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 序列对象集合
     */
    @Query(value = "select rslt from Rslt rslt where rslt.isSqxRslt = 1 and (rslt.createdDate between :startDate and :endDate) ")
    List<Object[]> doQueryByDaterange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
