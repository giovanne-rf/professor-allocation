package com.project.professor.allocation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course")

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "course")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Allocation> allocations;
}
