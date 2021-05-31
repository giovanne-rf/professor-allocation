package com.project.professor.allocation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentCompleteDTO extends DepartmentSimpleDTO {

    private List<ProfessorSimpleDTO> professors;
}
