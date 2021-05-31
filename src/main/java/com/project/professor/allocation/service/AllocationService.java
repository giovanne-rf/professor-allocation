package com.project.professor.allocation.service;

import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.exception.AllocationCollisionException;
import com.project.professor.allocation.exception.EntityInstanceNotFoundException;
import com.project.professor.allocation.repository.AllocationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Transactional
@Service
public class AllocationService {

    private final AllocationRepository allocationRepository;
    private final ProfessorService professorService;
    private final CourseService courseService;

    public AllocationService(AllocationRepository allocationRepository, ProfessorService professorService,
                             CourseService courseService) {
        super();
        this.allocationRepository = allocationRepository;
        this.professorService = professorService;
        this.courseService = courseService;
    }

    @Transactional(readOnly = true)
    public List<Allocation> findAll() {
        return allocationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Allocation findById(Long id) {
        return allocationRepository.findById(id).orElseThrow(getEntityInstanceNotFoundExceptionSupplier(id));
    }

    @Transactional(readOnly = true)
    public List<Allocation> findByProfessor(Long professorId) {
        return allocationRepository.findByProfessorId(professorId);
    }

    @Transactional(readOnly = true)
    public List<Allocation> findByCourse(Long courseId) {
        return allocationRepository.findByCourseId(courseId);
    }

    public Allocation save(Allocation allocation) {
        allocation.setId(null);
        return saveInternal(allocation);
    }

    public Allocation update(Allocation allocation) {
        Long id = allocation.getId();
        if (id == null || !allocationRepository.existsById(id)) {
            throw getEntityInstanceNotFoundExceptionSupplier(id).get();
        }

        return saveInternal(allocation);
    }

    public void deleteById(Long id) {
        if (id != null && allocationRepository.existsById(id)) {
            allocationRepository.deleteById(id);
        }
    }

    public void deleteAll() {
        allocationRepository.deleteAllInBatch();
    }

    private Allocation saveInternal(Allocation allocation) {
        if (!hasCollision(allocation)) {
            allocation = allocationRepository.save(allocation);

            Professor professor = allocation.getProfessor();
            professor = professorService.findById(professor.getId());
            allocation.setProfessor(professor);

            Course course = allocation.getCourse();
            course = courseService.findById(course.getId());
            allocation.setCourse(course);

            return allocation;
        } else {
            throw new AllocationCollisionException(allocation);
        }
    }

    boolean hasCollision(Allocation newAllocation) {
        boolean hasCollision = false;

        List<Allocation> currentAllocations = allocationRepository.findByProfessorId(newAllocation.getProfessor().getId());

        for (Allocation currentAllocation : currentAllocations) {
            hasCollision = hasCollision(currentAllocation, newAllocation);
            if (hasCollision) {
                break;
            }
        }

        return hasCollision;
    }

    private boolean hasCollision(Allocation currentAllocation, Allocation newAllocation) {
        return !currentAllocation.getId().equals(newAllocation.getId())
                && currentAllocation.getDayOfWeek() == newAllocation.getDayOfWeek()
                && currentAllocation.getStartHour().compareTo(newAllocation.getEndHour()) < 0
                && newAllocation.getStartHour().compareTo(currentAllocation.getEndHour()) < 0;
    }

    private Supplier<EntityInstanceNotFoundException> getEntityInstanceNotFoundExceptionSupplier(Long id) {
        return () -> new EntityInstanceNotFoundException(Allocation.class, id);
    }
}
