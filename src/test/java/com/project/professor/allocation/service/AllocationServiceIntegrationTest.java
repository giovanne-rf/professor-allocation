package com.project.professor.allocation.service;

import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.project.professor.allocation.test.TestEntityManagerUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
public class AllocationServiceIntegrationTest {

    private static final Long HOUR = TimeUnit.HOURS.toMillis(1);

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private TestEntityManager entityManager;

    /*
    Scenario:
                                3   4
    Current allocation:         |   |
    new allocation:     |   |
                        1   2
     */
    @Test
    public void hasCollision_whenNewEndTimeLessThanCurrentStartTime_thenHasCollisionIsFalse() {
        hasCollision(3, 4, 1, 2, false);
    }

    /*
    Scenario:
                                3   4
    Current allocation:         |   |
    new allocation:                     |   |
                                        5   6
     */
    @Test
    public void hasCollision_whenNewStartTimeBiggerThanCurrentEndTime_thenHasCollisionIsFalse() {
        hasCollision(3, 4, 5, 6, false);
    }

    /*
    Scenario:
                                3   4
    Current allocation:         |   |
    new allocation:         |   |
                            2   3
     */
    @Test
    public void hasCollision_whenNewEndTimeEqualsToCurrentStartTime_thenHasCollisionIsFalse() {
        hasCollision(3, 4, 2, 3, false);
    }

    /*
    Scenario:
                                3   4
    Current allocation:         |   |
    new allocation:                 |   |
                                    4   5
     */
    @Test
    public void hasCollision_whenNewStartTimeEqualsToCurrentEndTime_thenHasCollisionIsFalse() {
        hasCollision(3, 4, 4, 5, false);
    }

    /*
    Scenario:
                                3   4
    Current allocation:         |   |
    new allocation:             |   |
                                3   4
     */
    @Test
    public void hasCollision_whenTimesIsAllEquals_thenHasCollisionIsTrue() {
        hasCollision(3, 4, 3, 4, true);
    }

    /*
    Scenario:
                                3       5
    Current allocation:         |       |
    new allocation:         |       |
                            2       4
     */
    @Test
    public void hasCollision_whenHasIntersectionOnLeft_thenHasCollisionIsTrue() {
        hasCollision(3, 5, 2, 4, true);
    }

    /*
    Scenario:
                                3       5
    Current allocation:         |       |
    new allocation:                 |       |
                                    4       6
     */
    @Test
    public void hasCollision_whenHasIntersectionOnRight_thenHasCollisionIsTrue() {
        hasCollision(3, 5, 4, 6, true);
    }

    /*
    Scenario:
                                3   4
    Current allocation:         |   |
    new allocation:         |           |
                            2           5
     */
    @Test
    public void hasCollision_whenNewContainsCurrent_thenHasCollisionIsTrue() {
        hasCollision(3, 4, 2, 5, true);
    }

    /*
    Scenario:
                            2           5
    Current allocation:     |           |
    new allocation:             |   |
                                3   4
     */
    @Test
    public void hasCollision_whenCurrentContainsNew_thenHasCollisionIsTrue() {
        hasCollision(2, 5, 3, 4, true);
    }

    private void hasCollision(long currentStartTime, long currentEndTime,
                              long newStartTime, long newEndTime,
                              boolean expected) {
        // Arrange
        Department d1 = createDepartment(entityManager, 1);
        Professor p1 = createProfessor(entityManager, 1, d1);
        Course c1 = createCourse(entityManager, 1);

        createAllocation(entityManager, DayOfWeek.MONDAY, getTime(currentStartTime), getTime(currentEndTime), p1, c1);
        Allocation allocation = new Allocation(null, DayOfWeek.MONDAY, getTime(newStartTime), getTime(newEndTime), p1, c1);

        // Act
        boolean hasCollision = allocationService.hasCollision(allocation);

        // Assert
        assertThat(hasCollision).isEqualTo(expected);
    }

    private Date getTime(long multiplier) {
        return new Time((multiplier + 3) * HOUR);
    }
}
