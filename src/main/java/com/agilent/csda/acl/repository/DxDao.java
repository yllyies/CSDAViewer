package com.agilent.csda.acl.repository;

import com.agilent.csda.acl.model.Dx;
import org.springframework.data.jpa.domain.Specification;
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
public interface DxDao extends JpaRepository<Dx, BigDecimal>, JpaSpecificationExecutor<Dx>
{
    List<Dx> findAll(@Nullable Specification<Dx> var1);

    /**
     * 自定义查询条件
     *
     * @param params
     * @return
     */
    @Query(value = "select dx,rslt,p from Dx dx " +
            "left join Rslt rslt on dx.parentNodeId = rslt.nodeId " +
            "left join Project p on rslt.projectId = p.id " +
            "where dx.uploadedDate between :startDate and :endDate " +
            "and rslt.instrumentName in (:instrumentNames) ")
    List<Object[]> doQueryCustom(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                           @Param("instrumentNames") List<String> instrumentNames);

}
