package com.agilent.csda.acl.repository;

import java.util.List;

import com.agilent.csda.acl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, Long>
{
	List<User> findByName(String name);

	List<User> findByEmail(String email);

	List<User> findByEmailAndPassword(String email, String password);
}
