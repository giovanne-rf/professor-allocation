package com.project.professor.allocation.controller;

import com.project.professor.allocation.dto.DepartmentCompleteDTO;
import com.project.professor.allocation.dto.DepartmentCreationDTO;
import com.project.professor.allocation.dto.DepartmentSimpleDTO;
import com.project.professor.allocation.dto.ErrorDTO;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.mapper.DepartmentMapper;
import com.project.professor.allocation.service.DepartmentService;
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

@Api(tags = {"departments"})
@RestController
@RequestMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentMapper mapper;

    public DepartmentController(DepartmentService departmentService, DepartmentMapper mapper) {
        super();
        this.departmentService = departmentService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Find all departments")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DepartmentSimpleDTO>> findAll(@RequestParam(name = "name", required = false) String name) {
        List<Department> departments = departmentService.findAll(name);
        return new ResponseEntity<>(mapper.toSimpleDTO(departments), HttpStatus.OK);
    }

    @ApiOperation(value = "Find a department by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @GetMapping(path = "/{department_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DepartmentCompleteDTO> findById(@PathVariable(name = "department_id") Long id) {
        Department department = departmentService.findById(id);
        return new ResponseEntity<>(mapper.toCompleteDTO(department), HttpStatus.OK);
    }

    @ApiOperation(value = "Save a department")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DepartmentSimpleDTO> save(@Valid @RequestBody DepartmentCreationDTO departmentDTO) {
        Department department = departmentService.save(mapper.toEntity(departmentDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(department), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a department")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @PutMapping(path = "/{department_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DepartmentSimpleDTO> update(@PathVariable(name = "department_id") Long id,
                                                      @Valid @RequestBody DepartmentCreationDTO departmentDTO) {
        departmentDTO.setId(id);
        Department department = departmentService.update(mapper.toEntity(departmentDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(department), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a department")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @DeleteMapping(path = "/{department_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable(name = "department_id") Long id) {
        departmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete all departments")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAll() {
        departmentService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
