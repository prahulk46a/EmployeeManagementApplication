package com.example.demo.controllers;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dtos.EmployeeDTO;
import com.example.demo.services.EmployeeService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")

public class EmployeeController {
	
	private final EmployeeService employeeService;
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/employees")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployee(){
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}
	
	
	
	@PostMapping("/employees")
	public ResponseEntity<EmployeeDTO>create(@Valid @RequestBody EmployeeDTO dto){
		return ResponseEntity.ok(employeeService.createEmployee(dto));
	}
	
	@GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/employees/{id}")
 
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    
    //Paginated data fetch
    @GetMapping("/employees/list")
    public ResponseEntity<Page<EmployeeDTO>> getAllPaginated(
            @RequestParam(defaultValue  = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(employeeService.getAllEmployeesPaginated(page, size, sortBy, direction));
    }
	
}
