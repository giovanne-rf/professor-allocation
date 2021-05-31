package com.project.professor.allocation.service;

import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.exception.EntityInstanceNotFoundException;
import com.project.professor.allocation.repository.CourseRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Transactional
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        super();
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> findAll(String name) {
        if (name == null) {
            return courseRepository.findAll();
        } else {
            return courseRepository.findByNameContainingIgnoreCase(name);
        }
    }

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(getEntityInstanceNotFoundExceptionSupplier(id));
    }

    public Course save(Course course) {
        course.setId(null);
        return saveInternal(course);
    }

    public Course update(Course course) {
        Long id = course.getId();
        if (id == null || !courseRepository.existsById(id)) {
            throw getEntityInstanceNotFoundExceptionSupplier(id).get();
        }

        return saveInternal(course);
    }

    public void deleteById(Long id) {
        if (id != null && courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        }
    }

    public void deleteAll() {
        courseRepository.deleteAllInBatch();
    }

    private Course saveInternal(Course course) {
        return courseRepository.save(course);
    }

    private Supplier<EntityInstanceNotFoundException> getEntityInstanceNotFoundExceptionSupplier(Long id) {
        return () -> new EntityInstanceNotFoundException(Course.class, id);
    }
}
