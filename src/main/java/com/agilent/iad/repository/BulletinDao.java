package com.agilent.iad.repository;

import com.agilent.iad.model.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BulletinDao extends JpaRepository<Bulletin, String>, JpaSpecificationExecutor<Bulletin>
{

}
