package com.project.professor.allocation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class AllocationCreationDTO extends AllocationBaseDTO {

    @NotNull
    private Long professorId;

    @NotNull
    private Long courseId;
}
