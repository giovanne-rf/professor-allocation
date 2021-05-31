package com.project.professor.allocation.repository;

import com.project.professor.allocation.entity.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static com.project.professor.allocation.test.TestEntityManagerUtils.createDepartment;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DepartmentRepositoryIntegrationTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByNameContainingIgnoreCase_whenPassContainedName_thenReturnTheDepartmentList() {
        // Arrange
        Department d1 = createDepartment(entityManager, "Department of Physics 1");
        Department d2 = createDepartment(entityManager, "Department of Physics 2");
        Department d3 = createDepartment(entityManager, "Department of Physics 3");

        Department d4 = createDepartment(entityManager, "Department of Chemistry 1");
        Department d5 = createDepartment(entityManager, "Department of Chemistry 2");
        Department d6 = createDepartment(entityManager, "Department of Chemistry 3");

        // Act
        List<Department> departments1 = departmentRepository.findByNameContainingIgnoreCase("Phys");
        List<Department> departments2 = departmentRepository.findByNameContainingIgnoreCase("Chem");

        // Assert
        assertThat(departments1).contains(d1, d2, d3);
        assertThat(departments1).doesNotContain(d4, d5, d6);

        assertThat(departments2).contains(d4, d5, d6);
        assertThat(departments2).doesNotContain(d1, d2, d3);
    }

    @Test
    public void findByNameContainingIgnoreCase_whenPassNotContainedName_thenReturnEmptyDepartmentList() {
        // Arrange
        createDepartment(entityManager, "Department of Physics 1");
        createDepartment(entityManager, "Department of Physics 2");
        createDepartment(entityManager, "Department of Physics 3");

        // Act
        List<Department> departments = departmentRepository.findByNameContainingIgnoreCase("Chem");

        // Assert
        assertThat(departments).isEmpty();
    }
}
