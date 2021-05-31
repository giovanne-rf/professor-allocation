package com.project.professor.allocation.service;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.exception.EntityInstanceNotFoundException;
import com.project.professor.allocation.repository.DepartmentRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Transactional
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        super();
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Department> findAll(String name) {
        if (name == null) {
            return departmentRepository.findAll();
        } else {
            return departmentRepository.findByNameContainingIgnoreCase(name);
        }
    }

    @Transactional(readOnly = true)
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(getEntityInstanceNotFoundExceptionSupplier(id));
    }

    public Department save(Department department) {
        department.setId(null);
        return saveInternal(department);
    }

    public Department update(Department department) {
        Long id = department.getId();
        if (id == null || !departmentRepository.existsById(id)) {
            throw getEntityInstanceNotFoundExceptionSupplier(id).get();
        }

        return saveInternal(department);
    }

    public void deleteById(Long id) {
        if (id != null && departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        }
    }

    public void deleteAll() {
        departmentRepository.deleteAllInBatch();
    }

    private Department saveInternal(Department department) {
        return departmentRepository.save(department);
    }

    private Supplier<EntityInstanceNotFoundException> getEntityInstanceNotFoundExceptionSupplier(Long id) {
        return () -> new EntityInstanceNotFoundException(Department.class, id);
    }
}
