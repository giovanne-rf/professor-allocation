package com.project.professor.allocation.repository;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import org.h2.jdbc.JdbcSQLDataException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;

import static com.project.professor.allocation.test.TestEntityManagerUtils.createDepartment;
import static com.project.professor.allocation.test.TestEntityManagerUtils.createProfessor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ProfessorRepositoryIntegrationTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByNameContainingIgnoreCase_whenPassContainedName_thenReturnTheProfessorList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);
        Department d2 = createDepartment(entityManager, 2);

        Professor p1 = createProfessor(entityManager, "Fulano José", "CPF1", d1);
        Professor p2 = createProfessor(entityManager, "Cicrano José", "CPF2", d1);
        Professor p3 = createProfessor(entityManager, "Beltrano José", "CPF3", d1);

        Professor p4 = createProfessor(entityManager, "Fulano João", "CPF4", d2);
        Professor p5 = createProfessor(entityManager, "Cicrano João", "CPF5", d2);
        Professor p6 = createProfessor(entityManager, "Beltrano João", "CPF6", d2);

        // Act
        List<Professor> professors1 = professorRepository.findByNameContainingIgnoreCase("jOs");
        List<Professor> professors2 = professorRepository.findByNameContainingIgnoreCase("jOã");

        // Assert
        assertThat(professors1).contains(p1, p2, p3);
        assertThat(professors1).doesNotContain(p4, p5, p6);

        assertThat(professors2).contains(p4, p5, p6);
        assertThat(professors2).doesNotContain(p1, p2, p3);
    }

    @Test
    public void findByNameContainingIgnoreCase_whenPassNotContainedName_thenReturnEmptyProfessorList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        createProfessor(entityManager, "Fulano José", "CPF1", d1);
        createProfessor(entityManager, "Cicrano José", "CPF2", d1);
        createProfessor(entityManager, "Beltrano José", "CPF3", d1);

        // Act
        List<Professor> professors = professorRepository.findByNameContainingIgnoreCase("jOa");

        // Assert
        assertThat(professors).isEmpty();
    }

    @Test
    public void findByDepartmentId_whenPassDepartmentWithProfessors_thenReturnTheProfessorList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);
        Department d2 = createDepartment(entityManager, 2);

        Professor p1 = createProfessor(entityManager, 1, d1);
        Professor p2 = createProfessor(entityManager, 2, d1);
        Professor p3 = createProfessor(entityManager, 3, d2);
        Professor p4 = createProfessor(entityManager, 4, d2);

        // Act
        List<Professor> professors1 = professorRepository.findByDepartmentId(d1.getId());
        List<Professor> professors2 = professorRepository.findByDepartmentId(d2.getId());

        // Assert
        assertThat(professors1).contains(p1, p2);
        assertThat(professors1).doesNotContain(p3, p4);

        assertThat(professors2).contains(p3, p4);
        assertThat(professors2).doesNotContain(p1, p2);
    }

    @Test
    public void findByDepartmentId_whenPassDepartmentWithoutProfessors_thenReturnEmptyProfessorList() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);
        Department d2 = createDepartment(entityManager, 2);

        createProfessor(entityManager, 1, d1);
        createProfessor(entityManager, 2, d1);

        // Act
        List<Professor> professors = professorRepository.findByDepartmentId(d2.getId());

        // Assert
        assertThat(professors).isEmpty();
    }

    @Test
    public void save_whenPassWithValidFieldsAndIdNull_thenCreateInDatabaseAndReturnWithSameFieldsButIdNotNull() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        Professor p1 = new Professor(null, "P1", "CPF1", d1, Collections.emptyList());

        // Act
        Professor professor = professorRepository.save(p1);

        // Assert
        assertThat(professorRepository.findById(professor.getId())).isNotEmpty();

        assertThat(professor.getId()).isNotNull();
        assertThat(professor.getName()).isEqualTo(p1.getName());
        assertThat(professor.getCpf()).isEqualTo(p1.getCpf());
        assertThat(professor.getDepartment()).isEqualTo(p1.getDepartment());
        assertThat(professor.getAllocations()).isEqualTo(p1.getAllocations());
    }

    @Test
    public void save_whenPassWithValidFieldsAndIdNotNullThatNotExists_thenCreateInDatabaseAndReturnWithSameFieldsButReplacingId() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        Professor p1 = new Professor(1000L, "P1", "CPF1", d1, Collections.emptyList());

        // Act
        Professor professor = professorRepository.save(p1);

        // Assert
        assertThat(professorRepository.findById(professor.getId())).isNotEmpty();

        assertThat(professor.getId()).isNotEqualTo(p1.getId());
        assertThat(professor.getName()).isEqualTo(p1.getName());
        assertThat(professor.getCpf()).isEqualTo(p1.getCpf());
        assertThat(professor.getDepartment()).isEqualTo(p1.getDepartment());
        assertThat(professor.getAllocations()).isEqualTo(p1.getAllocations());
    }

    @Test
    public void save_whenPassWithValidFieldsAndIdNotNullThatExists_thenUpdateInDatabase() {
        // Arrange
        String newCPF = "CPF2";

        Department d1 = createDepartment(entityManager, 1);

        Professor p1 = createProfessor(entityManager, 1, d1);
        Professor p1Updated = new Professor(p1.getId(), p1.getName(), newCPF, p1.getDepartment(), p1.getAllocations());

        // Act
        Professor professor = professorRepository.save(p1Updated);

        // Assert
        Professor stored = professorRepository.findById(professor.getId()).orElse(null);
        assertThat(stored).isNotNull();

        assertThat(stored.getId()).isEqualTo(p1.getId());
        assertThat(stored.getName()).isEqualTo(p1.getName());
        assertThat(stored.getCpf()).isEqualTo(newCPF);
        assertThat(stored.getDepartment()).isEqualTo(p1.getDepartment());
        assertThat(stored.getAllocations()).isEqualTo(p1.getAllocations());
    }

    @Test
    public void save_whenNotNullableFieldsArePassedWithNullValue_thenThrowsDataIntegrityViolationException() {
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {

            // Arrange
            Professor p1 = new Professor(null, null, null, null, Collections.emptyList());

            // Act
            professorRepository.save(p1);

        });

        // Assert
        assertThat(ex).hasRootCauseInstanceOf(JdbcSQLIntegrityConstraintViolationException.class);
        assertThat(ex).getRootCause().hasMessageContaining("NULL not allowed for column");
    }

    @Test
    public void save_whenFieldIsTooLong_thenThrowsDataIntegrityViolationException() {
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {

            // Arrange
            Department d1 = createDepartment(entityManager, 1);

            Professor p1 = new Professor(null, "P1", String.format("%0999d", 1), d1, Collections.emptyList());

            // Act
            professorRepository.save(p1);

        });

        // Assert
        assertThat(ex).hasRootCauseInstanceOf(JdbcSQLDataException.class);
        assertThat(ex).getRootCause().hasMessageContaining("Value too long for column");
    }

    @Test
    public void save_whenUniqueFieldAlreadyExist_thenThrowsDataIntegrityViolationException() {
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {

            // Arrange
            Department d1 = createDepartment(entityManager, 1);

            Professor p1 = createProfessor(entityManager, 1, d1);
            Professor p2 = new Professor(null, "P2", p1.getCpf(), d1, Collections.emptyList());

            // Act
            professorRepository.save(p2);

        });

        // Assert
        assertThat(ex).hasRootCauseInstanceOf(JdbcSQLIntegrityConstraintViolationException.class);
        assertThat(ex).getRootCause().hasMessageContaining("Unique index or primary key violation");
    }

    @Test
    public void save_whenForeignKeyNotExist_thenThrowsDataIntegrityViolationException() {
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> {

            // Arrange
            Department d1 = new Department(1L, "D1", Collections.emptyList());

            Professor p1 = new Professor(null, "P1", "CPF1", d1, Collections.emptyList());

            // Act
            professorRepository.save(p1);

        });

        // Assert
        assertThat(ex).hasRootCauseInstanceOf(JdbcSQLIntegrityConstraintViolationException.class);
        assertThat(ex).getRootCause().hasMessageContaining("Referential integrity constraint violation");
    }

    @Test
    public void delete_whenPassIdThatExist_thenDeleteFromDatabase() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        Professor p1 = createProfessor(entityManager, 1, d1);

        // Act
        professorRepository.deleteById(p1.getId());

        // Assert
        assertThat(professorRepository.findById(p1.getId())).isEmpty();
    }

    @Test
    public void delete_whenPassNonexistentId_thenThrowsEmptyResultDataAccessException() {
        // Arrange
        Long nonexistentId = 0L;

        EmptyResultDataAccessException ex = assertThrows(EmptyResultDataAccessException.class, () -> {
            // Act
            professorRepository.deleteById(nonexistentId);
        });

        // Assert
        assertThat(ex).hasNoCause();
        assertThat(ex).hasMessage(String.format("No class %s entity with id %d exists!", Professor.class.getName(),
                nonexistentId));
    }

    @Test
    public void deleteAllInBatch_whenCalled_thenDeleteAllFromDatabase() {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);

        createProfessor(entityManager, 1, d1);
        createProfessor(entityManager, 2, d1);
        createProfessor(entityManager, 3, d1);
        createProfessor(entityManager, 4, d1);

        // Act
        professorRepository.deleteAllInBatch();

        // Assert
        assertThat(professorRepository.findAll()).isEmpty();
    }
}
