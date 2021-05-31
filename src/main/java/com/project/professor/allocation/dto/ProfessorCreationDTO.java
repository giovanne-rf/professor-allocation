package com.project.professor.allocation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ProfessorCreationDTO extends ProfessorBaseDTO {

    @NotNull
    private Long departmentId;
}
