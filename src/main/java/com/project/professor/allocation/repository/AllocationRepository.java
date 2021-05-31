package com.project.professor.allocation.repository;

import com.project.professor.allocation.entity.Allocation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {

    @EntityGraph(attributePaths = {"professor", "course", "professor.department"})
    @Override
    List<Allocation> findAll();

    @EntityGraph(attributePaths = {"professor", "course", "professor.department"})
    @Override
    Optional<Allocation> findById(Long id);

    @EntityGraph(attributePaths = {"professor", "course", "professor.department"})
    List<Allocation> findByProfessorId(Long professorId);

    @EntityGraph(attributePaths = {"professor", "course", "professor.department"})
    List<Allocation> findByCourseId(Long courseId);
}
