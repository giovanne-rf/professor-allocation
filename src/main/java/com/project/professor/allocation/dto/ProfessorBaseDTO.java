package com.project.professor.allocation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class ProfessorBaseDTO {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")
    private String cpf;
}
