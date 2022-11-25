package com.agilent.csda.acl.domain;

import java.util.List;

import com.agilent.csda.acl.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
	List<Role> findByName(String name);
}
