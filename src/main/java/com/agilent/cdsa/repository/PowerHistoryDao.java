package com.agilent.cdsa.repository;

import com.agilent.cdsa.model.PowerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PowerHistoryDao extends JpaRepository<PowerHistory, String>, JpaSpecificationExecutor<PowerHistory>
{

    /**
     * 根据IP集合查询，并按照创建时间降序
     *
     * @param ips 智能插座IP集合
     * @return 结果集
     */
    List<PowerHistory> findByIpInOrderByCreatedDateDesc(List<String> ips);

    /**
     * 根据IP和记录时间查询
     *
     * @param ips 智能插座IP集合
     * @param createdDate 记录时间
     * @return 结果集
     */
    List<PowerHistory> findByIpInAndCreatedDateEquals(List<String> ips, Timestamp createdDate);
}
