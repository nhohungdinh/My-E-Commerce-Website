package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userDNH = new User("hungdn@gmail.com", "Mayman123123", "Hung", "Dinh Nho");
		userDNH.addRole(roleAdmin);
		User savedUser = repo.save(userDNH);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		User userNTL = new User("lynt@gmail.com", "Mayman123123", "Ly", "Nguyen Thi");
		Role roleAssistant = new Role(2);
		Role roleEditor = new Role(3);
		userNTL.addRole(roleEditor);
		userNTL.addRole(roleAssistant);
		User savedUser = repo.save(userNTL);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUser() {
		Iterable<User> listUser = repo.findAll();
		listUser.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userDNH = repo.findById(1).get();
		System.out.println(userDNH);
		assertThat(userDNH).isNotNull();
		
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userDNH = repo.findById(1).get();
		userDNH.setEmail("hungdnprogrammer@gmail.com");
		userDNH.setEnabled(true);
		repo.save(userDNH);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userNTL = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(4);
		userNTL.getRoles().remove(roleEditor);
		userNTL.addRole(roleSalesperson);
		repo.save(userNTL);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "reminder@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 100;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		List<User> listUser = page.getContent();
		listUser.forEach(user -> System.out.println(user));
		assertThat(listUser.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUser() {
		String keyword = "bruce";
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		List<User> listUser = page.getContent();
		listUser.forEach(user -> System.out.println(user));
		assertThat(listUser.size()).isGreaterThan(0);
	}
}
