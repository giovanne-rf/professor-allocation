package com.project.professor.allocation.mapper;

import com.project.professor.allocation.dto.CourseCompleteDTO;
import com.project.professor.allocation.dto.CourseCreationDTO;
import com.project.professor.allocation.dto.CourseSimpleDTO;
import com.project.professor.allocation.entity.Course;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    private final ModelMapper modelMapper;

    public CourseMapper() {
        this.modelMapper = new ModelMapper();
    }

    public List<CourseSimpleDTO> toSimpleDTO(List<Course> courses) {
        return courses.stream().map(this::toSimpleDTO).collect(Collectors.toList());
    }

    public CourseSimpleDTO toSimpleDTO(Course course) {
        return modelMapper.map(course, CourseSimpleDTO.class);
    }

    public CourseCompleteDTO toCompleteDTO(Course course) {
        return modelMapper.map(course, CourseCompleteDTO.class);
    }

    public Course toEntity(CourseCreationDTO courseCreationDTO) {
        return modelMapper.map(courseCreationDTO, Course.class);
    }
}
