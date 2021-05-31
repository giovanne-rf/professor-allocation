package com.project.professor.allocation.test;

import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TestEntityManagerUtils {

    public static Department createDepartment(TestEntityManager entityManager, int index) {
        return createDepartment(entityManager, String.format("D%d", index));
    }

    public static Department createDepartment(TestEntityManager entityManager, String name) {
        Department entity = entityManager.persistFlushFind(new Department(null, name, Collections.emptyList()));
        assertThat(entity).isNotNull();
        entityManager.flush();
        return entity;
    }

    public static Professor createProfessor(TestEntityManager entityManager, int index, Department department) {
        return createProfessor(entityManager, String.format("P%d", index), String.format("CPF%d", index), department);
    }

    public static Professor createProfessor(TestEntityManager entityManager, String name, String cpf, Department department) {
        Professor entity = entityManager.persistFlushFind(new Professor(null, name, cpf, department, Collections.emptyList()));
        assertThat(entity).isNotNull();
        entityManager.flush();
        return entity;
    }

    public static Course createCourse(TestEntityManager entityManager, int index) {
        return createCourse(entityManager, String.format("C%d", index));
    }

    public static Course createCourse(TestEntityManager entityManager, String name) {
        Course entity = entityManager.persistFlushFind(new Course(null, name, Collections.emptyList()));
        assertThat(entity).isNotNull();
        entityManager.flush();
        return entity;
    }

    public static Allocation createAllocation(TestEntityManager entityManager, DayOfWeek dayOfWeek, Date startHour,
                                              Date endHour, Professor professor, Course course) {
        Allocation entity = entityManager.persistFlushFind(new Allocation(null, dayOfWeek, startHour, endHour, professor, course));
        assertThat(entity).isNotNull();
        entityManager.flush();
        return entity;
    }
}
