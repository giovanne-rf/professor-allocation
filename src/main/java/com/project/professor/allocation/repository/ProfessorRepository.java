package com.project.professor.allocation.repository;

import com.project.professor.allocation.entity.Professor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @EntityGraph(attributePaths = {"department"})
    @Override
    List<Professor> findAll();

    @EntityGraph(attributePaths = {"department"})
    @Override
    Optional<Professor> findById(Long id);

    @EntityGraph(attributePaths = {"department"})
    List<Professor> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"department"})
    List<Professor> findByDepartmentId(Long departmentId);
}
