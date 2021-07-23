package com.arelance.test.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arelance.test.api.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>{
	
	Optional<Department> findByName(String name);
	boolean existsByName(String name);
	
}
