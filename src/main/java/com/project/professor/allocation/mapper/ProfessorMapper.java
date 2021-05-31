package com.project.professor.allocation.mapper;

import com.project.professor.allocation.dto.ProfessorCompleteDTO;
import com.project.professor.allocation.dto.ProfessorCreationDTO;
import com.project.professor.allocation.dto.ProfessorSimpleDTO;
import com.project.professor.allocation.entity.Professor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfessorMapper {

    private final ModelMapper modelMapper;

    public ProfessorMapper() {
        this.modelMapper = new ModelMapper();
    }

    public List<ProfessorSimpleDTO> toSimpleDTO(List<Professor> professors) {
        return professors.stream().map(this::toSimpleDTO).collect(Collectors.toList());
    }

    public ProfessorSimpleDTO toSimpleDTO(Professor professor) {
        return modelMapper.map(professor, ProfessorSimpleDTO.class);
    }

    public ProfessorCompleteDTO toCompleteDTO(Professor professor) {
        return modelMapper.map(professor, ProfessorCompleteDTO.class);
    }

    public Professor toEntity(ProfessorCreationDTO professorCreationDTO) {
        return modelMapper.map(professorCreationDTO, Professor.class);
    }
}
