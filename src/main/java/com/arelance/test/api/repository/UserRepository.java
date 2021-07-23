package com.arelance.test.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arelance.test.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findById(int id);
	Optional<User> findByEmail(String email);
	boolean existsById(String id);
	boolean existsByEmail(String email);
	List<User> findByNameContaining(String name);
	@Query(value = "SELECT * FROM user WHERE id IN"
	     + "(SELECT user_id FROM department_user WHERE department_id = ?1)", nativeQuery = true)
	List<User> findByDepartment(int id);
}
