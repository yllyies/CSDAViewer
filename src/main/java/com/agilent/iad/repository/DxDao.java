package com.agilent.iad.repository;

import com.agilent.iad.model.Dx;
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
public interface DxDao extends JpaRepository<Dx, BigDecimal>, JpaSpecificationExecutor<Dx> {
    List<Dx> findAll(@Nullable Specification<Dx> var1);

    /**
     * @param startDate
     * @param endDate
     * @param instrumentNames
     * @return
     */
    @Query(value = "select dx,rslt,p from Dx dx " +
            "left join Rslt rslt on dx.parentNodeId = rslt.nodeId " +
            "left join Project p on rslt.projectId = p.id " +
            "where dx.updatedDate between :startDate and :endDate " +
            "and rslt.instrumentName in (:instrumentNames) ")
    List<Object[]> doQueryInstrumentNames(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                          @Param("instrumentNames") List<String> instrumentNames);

    @Query(value = "select dx,rslt,p from Dx dx " +
            "left join Rslt rslt on dx.parentNodeId = rslt.nodeId " +
            "left join Project p on rslt.projectId = p.id " +
            "where dx.updatedDate between :startDate and :endDate " +
            "and p.name in (:projectNames) ")
    List<Object[]> doQueryProjectNames(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                       @Param("projectNames") List<String> projectNames);

    @Query(value = "select dx,rslt,p from Dx dx " +
            "left join Rslt rslt on dx.parentNodeId = rslt.nodeId " +
            "left join Project p on rslt.projectId = p.id " +
            "where dx.updatedDate between :startDate and :endDate " +
            "and rslt.creator in (:creatorNames) ")
    List<Object[]> doQueryCreatorNames(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                       @Param("creatorNames") List<String> creatorNames);

}
