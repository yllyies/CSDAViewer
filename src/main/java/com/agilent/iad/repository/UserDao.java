package com.agilent.iad.repository;

import com.agilent.iad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, Long>
{
	User findByName(String name);

	User findByEmail(String email);

	User findByEmailAndPassword(String email, String password);
}
