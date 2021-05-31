package com.project.professor.allocation.repository;

import com.project.professor.allocation.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static com.project.professor.allocation.test.TestEntityManagerUtils.createCourse;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CourseRepositoryIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByNameContainingIgnoreCase_whenPassContainedName_thenReturnTheCourseList() {
        // Arrange
        Course c1 = createCourse(entityManager, "Physics 1");
        Course c2 = createCourse(entityManager, "Physics 2");
        Course c3 = createCourse(entityManager, "Physics 3");

        Course c4 = createCourse(entityManager, "Chemistry 1");
        Course c5 = createCourse(entityManager, "Chemistry 2");
        Course c6 = createCourse(entityManager, "Chemistry 3");

        // Act
        List<Course> courses1 = courseRepository.findByNameContainingIgnoreCase("Phys");
        List<Course> courses2 = courseRepository.findByNameContainingIgnoreCase("Chem");

        // Assert
        assertThat(courses1).contains(c1, c2, c3);
        assertThat(courses1).doesNotContain(c4, c5, c6);

        assertThat(courses2).contains(c4, c5, c6);
        assertThat(courses2).doesNotContain(c1, c2, c3);
    }

    @Test
    public void findByNameContainingIgnoreCase_whenPassNotContainedName_thenReturnEmptyCourseList() {
        // Arrange
        createCourse(entityManager, "Physics 1");
        createCourse(entityManager, "Physics 2");
        createCourse(entityManager, "Physics 3");

        // Act
        List<Course> courses = courseRepository.findByNameContainingIgnoreCase("Chem");

        // Assert
        assertThat(courses).isEmpty();
    }
}
