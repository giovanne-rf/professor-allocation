package com.project.professor.allocation.controller;

import com.project.professor.allocation.dto.ErrorDTO;
import com.project.professor.allocation.dto.ProfessorCompleteDTO;
import com.project.professor.allocation.dto.ProfessorCreationDTO;
import com.project.professor.allocation.dto.ProfessorSimpleDTO;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.mapper.ProfessorMapper;
import com.project.professor.allocation.service.ProfessorService;
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

@Api(tags = {"professors"})
@RestController
@RequestMapping(path = "/professors", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfessorController {

    private final ProfessorService professorService;
    private final ProfessorMapper mapper;

    public ProfessorController(ProfessorService professorService, ProfessorMapper mapper) {
        super();
        this.professorService = professorService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Find all professors")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProfessorSimpleDTO>> findAll(@RequestParam(name = "name", required = false) String name) {
        List<Professor> professors = professorService.findAll(name);
        return new ResponseEntity<>(mapper.toSimpleDTO(professors), HttpStatus.OK);
    }

    @ApiOperation(value = "Find a professor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @GetMapping(path = "/{professor_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProfessorCompleteDTO> findById(@PathVariable(name = "professor_id") Long id) {
        Professor professor = professorService.findById(id);
        return new ResponseEntity<>(mapper.toCompleteDTO(professor), HttpStatus.OK);
    }

    @ApiOperation(value = "Find professors by department")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @GetMapping(path = "/department/{department_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProfessorSimpleDTO>> findByDepartment(@PathVariable(name = "department_id") Long id) {
        List<Professor> professors = professorService.findByDepartment(id);
        return new ResponseEntity<>(mapper.toSimpleDTO(professors), HttpStatus.OK);
    }

    @ApiOperation(value = "Save a professor")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProfessorSimpleDTO> save(@Valid @RequestBody ProfessorCreationDTO professorDTO) {
        Professor professor = professorService.save(mapper.toEntity(professorDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(professor), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a professor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @PutMapping(path = "/{professor_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProfessorSimpleDTO> update(@PathVariable(name = "professor_id") Long id,
                                                     @Valid @RequestBody ProfessorCreationDTO professorDTO) {
        professorDTO.setId(id);
        Professor professor = professorService.update(mapper.toEntity(professorDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(professor), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a professor")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @DeleteMapping(path = "/{professor_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable(name = "professor_id") Long id) {
        professorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete all professors")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAll() {
        professorService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
