package com.project.professor.allocation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "department")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Professor> professors;
}
