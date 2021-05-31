package com.project.professor.allocation.service;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.exception.EntityInstanceNotFoundException;
import com.project.professor.allocation.repository.ProfessorRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Transactional
@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final DepartmentService departmentService;

    public ProfessorService(ProfessorRepository professorRepository, DepartmentService departmentService) {
        super();
        this.professorRepository = professorRepository;
        this.departmentService = departmentService;
    }

    @Transactional(readOnly = true)
    public List<Professor> findAll(String name) {
        if (name == null) {
            return professorRepository.findAll();
        } else {
            return professorRepository.findByNameContainingIgnoreCase(name);
        }
    }

    @Transactional(readOnly = true)
    public Professor findById(Long id) {
        return professorRepository.findById(id).orElseThrow(getEntityInstanceNotFoundExceptionSupplier(id));
    }

    @Transactional(readOnly = true)
    public List<Professor> findByDepartment(Long departmentId) {
        return professorRepository.findByDepartmentId(departmentId);
    }

    public Professor save(Professor professor) {
        professor.setId(null);
        return saveInternal(professor);
    }

    public Professor update(Professor professor) {
        Long id = professor.getId();
        if (id == null || !professorRepository.existsById(id)) {
            throw getEntityInstanceNotFoundExceptionSupplier(id).get();
        }

        return saveInternal(professor);
    }

    public void deleteById(Long id) {
        if (id != null && professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
        }
    }

    public void deleteAll() {
        professorRepository.deleteAllInBatch();
    }

    private Professor saveInternal(Professor professor) {
        professor = professorRepository.save(professor);

        Department department = professor.getDepartment();
        department = departmentService.findById(department.getId());
        professor.setDepartment(department);

        return professor;
    }

    private Supplier<EntityInstanceNotFoundException> getEntityInstanceNotFoundExceptionSupplier(Long id) {
        return () -> new EntityInstanceNotFoundException(Professor.class, id);
    }
}
