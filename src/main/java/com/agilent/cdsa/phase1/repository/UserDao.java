package com.agilent.cdsa.phase1.repository;

import com.agilent.cdsa.phase1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, Long>
{
	User findByName(String name);

	User findByEmail(String email);

	User findByEmailAndPassword(String email, String password);
}
