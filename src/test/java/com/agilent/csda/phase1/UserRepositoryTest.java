package com.agilent.csda.phase1;

import com.agilent.csda.phase1.model.User;
import com.agilent.csda.phase1.repository.UserDao;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest
{
    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private UserDao repository;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@After
	public void tearDown() throws Exception
	{
	}
	
	@Test
	public void testFindByName()
	{
		assertTrue("Should not find user", repository.findByName("Michael123").size() == 0);
		
		List<User> users = repository.findByName("Michael");
		assertTrue("Should find user", users.size() > 0);
		verifyUser(users.get(0), "Michael", null);
	}
	
	@Test
	public void testFindByEmail()
	{
		assertTrue("Should not find user", repository.findByEmail("billy@hot.com").size() == 0);

		List<User> users = repository.findByEmail("billy@hotmail.com");
		assertTrue("Found user", users.size() > 0);
		verifyUser(users.get(0), "Billy", null);
	}
	
	@Test
	public void testFindByEmailAndPassword()
	{
		assertTrue("Should not find user", repository.findByEmailAndPassword("billy@hotmail.com", "1234").size() == 0);
		
		List<User> users = repository.findByEmailAndPassword("billy@hotmail.com", "123");
		assertTrue("Found user", users.size() > 0);
		verifyUser(users.get(0), "Billy", null);
	}
	
	private void verifyUser(User user, String name, String roleName)
	{
		// TODO Steven Lou 2019-09-25 - implement test for roles 
		assertTrue("Name is Michael", user.getName().equals(name));
//		user.getRoles().forEach(role -> assertTrue("Role is set", role.getName() != null));
	}
}
