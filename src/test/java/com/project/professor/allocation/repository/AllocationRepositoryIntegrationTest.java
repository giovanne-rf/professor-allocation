package com.project.professor.allocation.repository;

import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

import static com.project.professor.allocation.test.TestEntityManagerUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AllocationRepositoryIntegrationTest {

    @Autowired
    private AllocationRepository allocationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByProfessorId_whenPassProfessorWithAllocations_thenReturnTheAllocationList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);
        Department d2 = createDepartment(entityManager, 2);

        Professor p1 = createProfessor(entityManager, 1, d1);
        Professor p2 = createProfessor(entityManager, 2, d2);

        Course c1 = createCourse(entityManager, 1);
        Course c2 = createCourse(entityManager, 2);

        Allocation a1 = createAllocation(entityManager, DayOfWeek.MONDAY, new Date(), new Date(), p1, c1);
        Allocation a2 = createAllocation(entityManager, DayOfWeek.SUNDAY, new Date(), new Date(), p1, c2);
        Allocation a3 = createAllocation(entityManager, DayOfWeek.WEDNESDAY, new Date(), new Date(), p2, c1);
        Allocation a4 = createAllocation(entityManager, DayOfWeek.FRIDAY, new Date(), new Date(), p2, c2);

        // Act
        List<Allocation> allocations1 = allocationRepository.findByProfessorId(p1.getId());
        List<Allocation> allocations2 = allocationRepository.findByProfessorId(p2.getId());

        // Assert
        assertThat(allocations1).contains(a1, a2);
        assertThat(allocations1).doesNotContain(a3, a4);

        assertThat(allocations2).contains(a3, a4);
        assertThat(allocations2).doesNotContain(a1, a2);
    }

    @Test
    public void findByProfessorId_whenPassProfessorWithoutAllocations_thenReturnEmptyAllocationList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        Professor p1 = createProfessor(entityManager, 1, d1);
        Professor p2 = createProfessor(entityManager, 2, d1);

        Course c1 = createCourse(entityManager, 1);

        createAllocation(entityManager, DayOfWeek.MONDAY, new Date(), new Date(), p1, c1);
        createAllocation(entityManager, DayOfWeek.SUNDAY, new Date(), new Date(), p1, c1);

        // Act
        List<Allocation> allocations = allocationRepository.findByProfessorId(p2.getId());

        // Assert
        assertThat(allocations).isEmpty();
    }

    @Test
    public void findByCourseId_whenPassCourseWithAllocations_thenReturnTheAllocationList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);
        Department d2 = createDepartment(entityManager, 2);

        Professor p1 = createProfessor(entityManager, 1, d1);
        Professor p2 = createProfessor(entityManager, 2, d2);

        Course c1 = createCourse(entityManager, 1);
        Course c2 = createCourse(entityManager, 2);

        Allocation a1 = createAllocation(entityManager, DayOfWeek.MONDAY, new Date(), new Date(), p1, c1);
        Allocation a2 = createAllocation(entityManager, DayOfWeek.SUNDAY, new Date(), new Date(), p1, c2);
        Allocation a3 = createAllocation(entityManager, DayOfWeek.WEDNESDAY, new Date(), new Date(), p2, c1);
        Allocation a4 = createAllocation(entityManager, DayOfWeek.FRIDAY, new Date(), new Date(), p2, c2);

        // Act
        List<Allocation> allocations1 = allocationRepository.findByCourseId(c1.getId());
        List<Allocation> allocations2 = allocationRepository.findByCourseId(c2.getId());

        // Assert
        assertThat(allocations1).contains(a1, a3);
        assertThat(allocations1).doesNotContain(a2, a4);

        assertThat(allocations2).contains(a2, a4);
        assertThat(allocations2).doesNotContain(a1, a3);
    }

    @Test
    public void findByCourseId_whenPassCourseWithoutAllocations_thenReturnEmptyAllocationList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        Professor p1 = createProfessor(entityManager, 1, d1);

        Course c1 = createCourse(entityManager, 1);
        Course c2 = createCourse(entityManager, 2);

        createAllocation(entityManager, DayOfWeek.MONDAY, new Date(), new Date(), p1, c1);
        createAllocation(entityManager, DayOfWeek.SUNDAY, new Date(), new Date(), p1, c1);

        // Act
        List<Allocation> allocations = allocationRepository.findByCourseId(c2.getId());

        // Assert
        assertThat(allocations).isEmpty();
    }
}
