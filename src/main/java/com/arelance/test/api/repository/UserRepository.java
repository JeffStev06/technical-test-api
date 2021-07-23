package com.arelance.test.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arelance.test.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findById(int id);
	Optional<User> findByEmail(String email);
	boolean existsById(String id);
	boolean existsByEmail(String email);
}
