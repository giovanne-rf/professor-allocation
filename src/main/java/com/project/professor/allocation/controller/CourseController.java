package com.project.professor.allocation.controller;

import com.project.professor.allocation.dto.CourseCompleteDTO;
import com.project.professor.allocation.dto.CourseCreationDTO;
import com.project.professor.allocation.dto.CourseSimpleDTO;
import com.project.professor.allocation.dto.ErrorDTO;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.mapper.CourseMapper;
import com.project.professor.allocation.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"courses"})
@RestController
@RequestMapping(path = "/courses", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper mapper;

    public CourseController(CourseService courseService, CourseMapper mapper) {
        super();
        this.courseService = courseService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Find all courses")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CourseSimpleDTO>> findAll(@RequestParam(name = "name", required = false) String name) {
        List<Course> courses = courseService.findAll(name);
        return new ResponseEntity<>(mapper.toSimpleDTO(courses), HttpStatus.OK);
    }

    @ApiOperation(value = "Find a course by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @GetMapping(path = "/{course_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CourseCompleteDTO> findById(@PathVariable(name = "course_id") Long id) {
        Course course = courseService.findById(id);
        return new ResponseEntity<>(mapper.toCompleteDTO(course), HttpStatus.OK);
    }

    @ApiOperation(value = "Save a course")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CourseSimpleDTO> save(@Valid @RequestBody CourseCreationDTO courseDTO) {
        Course course = courseService.save(mapper.toEntity(courseDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(course), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a course")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @PutMapping(path = "/{course_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CourseSimpleDTO> update(@PathVariable(name = "course_id") Long id,
                                                  @Valid @RequestBody CourseCreationDTO courseDTO) {
        courseDTO.setId(id);
        Course course = courseService.update(mapper.toEntity(courseDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(course), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a course")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @DeleteMapping(path = "/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable(name = "course_id") Long id) {
        courseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete all courses")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAll() {
        courseService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
