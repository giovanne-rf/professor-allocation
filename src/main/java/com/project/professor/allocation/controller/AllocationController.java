package com.project.professor.allocation.controller;

import com.project.professor.allocation.dto.AllocationCompleteDTO;
import com.project.professor.allocation.dto.AllocationCreationDTO;
import com.project.professor.allocation.dto.AllocationSimpleDTO;
import com.project.professor.allocation.dto.ErrorDTO;
import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.mapper.AllocationMapper;
import com.project.professor.allocation.service.AllocationService;
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

@Api(tags = {"allocations"})
@RestController
@RequestMapping(path = "/allocations", produces = MediaType.APPLICATION_JSON_VALUE)
public class AllocationController {

    private final AllocationService allocationService;
    private final AllocationMapper mapper;

    public AllocationController(AllocationService allocationService, AllocationMapper mapper) {
        super();
        this.allocationService = allocationService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "Find all allocations")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AllocationSimpleDTO>> findAll() {
        List<Allocation> allocations = allocationService.findAll();
        return new ResponseEntity<>(mapper.toSimpleDTO(allocations), HttpStatus.OK);
    }

    @ApiOperation(value = "Find an allocation by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @GetMapping(path = "/{allocation_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AllocationCompleteDTO> findById(@PathVariable(name = "allocation_id") Long id) {
        Allocation allocation = allocationService.findById(id);
        return new ResponseEntity<>(mapper.toCompleteDTO(allocation), HttpStatus.OK);
    }

    @ApiOperation(value = "Find allocations by professor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @GetMapping(path = "/professor/{professor_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AllocationSimpleDTO>> findByProfessor(@PathVariable(name = "professor_id") Long id) {
        List<Allocation> allocations = allocationService.findByProfessor(id);
        return new ResponseEntity<>(mapper.toSimpleDTO(allocations), HttpStatus.OK);
    }

    @ApiOperation(value = "Find allocations by course")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @GetMapping(path = "/course/{course_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AllocationSimpleDTO>> findByCourse(@PathVariable(name = "course_id") Long id) {
        List<Allocation> allocations = allocationService.findByCourse(id);
        return new ResponseEntity<>(mapper.toSimpleDTO(allocations), HttpStatus.OK);
    }

    @ApiOperation(value = "Save an allocation")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AllocationSimpleDTO> save(@Valid @RequestBody AllocationCreationDTO allocationDTO) {
        Allocation allocation = allocationService.save(mapper.toEntity(allocationDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(allocation), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update an allocation")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class)
    })
    @PutMapping(path = "/{allocation_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AllocationSimpleDTO> update(@PathVariable(name = "allocation_id") Long id,
                                                      @Valid @RequestBody AllocationCreationDTO allocationDTO) {
        allocationDTO.setId(id);
        Allocation allocation = allocationService.update(mapper.toEntity(allocationDTO));
        return new ResponseEntity<>(mapper.toSimpleDTO(allocation), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an allocation")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class)
    })
    @DeleteMapping(path = "/{allocation_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable(name = "allocation_id") Long id) {
        allocationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete all allocations")
    @ApiResponses({
            @ApiResponse(code = 204, message = "No Content")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAll() {
        allocationService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
