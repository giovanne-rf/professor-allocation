package com.project.professor.allocation.controller;

import com.project.professor.allocation.dto.ErrorDTO;
import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.mapper.AllocationMapper;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;

import static com.project.professor.allocation.test.TestEntityManagerUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
public class AllocationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AllocationMapper mapper;

    private String baseUrl;

    @BeforeEach
    public void beforeEach() {
        baseUrl = String.format("http://127.0.0.1:%s/allocation", port);
    }

    @Test
    public void save_when_then() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        Professor p1 = createProfessor(entityManager, 1, d1);

        Course c1 = createCourse(entityManager, 1);

        Allocation a1 = createAllocation(entityManager, DayOfWeek.MONDAY, new Date(), new Date(), p1, c1);

        // Act
        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(baseUrl, new DTO(a1), ErrorDTO.class);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Data
    public static class DTO {
        String dayOfWeek;
        String startHour;
        String endHour;
        Long courseId;
        Long professorId;

        public DTO(Allocation allocation) {
            dayOfWeek = allocation.getDayOfWeek().toString();
            startHour = new SimpleDateFormat("HH:mmZ").format(allocation.getStartHour());
            endHour = new SimpleDateFormat("HH:mmZ").format(allocation.getEndHour());
            courseId = allocation.getCourse().getId();
            professorId = allocation.getProfessor().getId();
        }
    }
}
