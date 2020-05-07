package com.cts.employeeService.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cts.employeeService.entityClass.Delivarable;
import com.cts.employeeService.entityClass.User;
<<<<<<< HEAD
=======
import com.cts.employeeService.exceptions.customExceptions.ErrorResponse;
import com.cts.employeeService.exceptions.customExceptions.IdNotFoundException;
>>>>>>> 39a199339ad764d7c6d84b5e6f7ed66f88a6f4a9
import com.cts.employeeService.modelClass.DelivarableModel;
import com.cts.employeeService.modelClass.DelivarableStatus;
import com.cts.employeeService.repository.UserRepository;
import com.cts.employeeService.service.EmployeeService;

import io.swagger.annotations.Api;

@RestController
@Api
@RefreshScope
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	@Autowired
	private UserRepository userRepository;
	
	
	@PostMapping("/saveDelivarable")
	public Delivarable saveDelivarable(@RequestBody DelivarableModel delivarableModel ) {
		
		if(StringUtils.isBlank(delivarableModel.getProjectname())) {
			throw new ValidationException("Project name can not be blank");
		}
		else {
		return this.employeeService.saveDelivarable(delivarableModel);
		}
	}
	
	@PutMapping("/updateRatingStatus")
	public Delivarable updatedelivarableRatingStatus(@RequestBody DelivarableStatus delivarableStatus) {
		if(delivarableStatus.getEmployeeId()==null) {
			throw new ValidationException("Employee Id can not be blank");
		}
		else if(delivarableStatus.getDelivarableId()==null) {
			throw new ValidationException("Deliverable Id can not be blank");
		}else {
		return this.employeeService.updateDelivarableRatingStatus(delivarableStatus);
		}
	}
	
	@PutMapping("/updateReviewStatus")
	public Delivarable updatedelivarableReviewStatus(@RequestBody DelivarableStatus delivarableStatus) {
		if(delivarableStatus.getEmployeeId()==null) {
			throw new ValidationException("Employee Id can not be blank");
		}
		else if(delivarableStatus.getDelivarableId()==null) {
			throw new ValidationException("Deliverable Id can not be blank");
		}else if (StringUtils.isBlank(delivarableStatus.getRemarks())){
		throw new ValidationException("Remarks can not be empty");
		}
		else {
		return this.employeeService.updateDelivarableReviewStatus(delivarableStatus);
		}
	}
	
	
	@GetMapping("/employeeIds/{managerId}")
	public ResponseEntity<List<Integer>> getEmployeeIdsBymanagerId(@PathVariable Integer managerId){
		List<User> d = this.userRepository.findBymanagerId(managerId);
		if(d.size()!=0) {
			return this.employeeService.getEmployeeIds(managerId);
		}
			else {
					throw new IdNotFoundException("No employee with maganer id "+managerId+" not found");		
			}
		}
	@GetMapping("/getDelivarables/{employeeId}")
	public ResponseEntity<List<Delivarable>> getDelivarablesByEmployeeId(@PathVariable Integer employeeId){
		Optional<User> d = this.userRepository.findById(employeeId);
		if(d.isPresent()) {
		return this.employeeService.getDelivarablesByEmployeeId(employeeId);
	}
		else {
				throw new IdNotFoundException("Employee With id "+employeeId+" not found");		
		}
	}
	
	
	@ExceptionHandler 
	public ResponseEntity<ErrorResponse> idNotFoundHandler(IdNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), 
															  HttpStatus.NOT_FOUND.value(), 
															  System.currentTimeMillis());
		ResponseEntity<ErrorResponse> response =
										new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
		
		return response;
	}
	
<<<<<<< HEAD
	@GetMapping("/userDetails/{employeeId}/{managerId}")
	public ResponseEntity<User> getDetailsByEmployeeIdAndBymanagerId(@PathVariable Integer employeeId,@PathVariable Integer managerId){
		return this.employeeService.getUserByManagerId(employeeId, managerId);
	}
	
}
=======
}
>>>>>>> 39a199339ad764d7c6d84b5e6f7ed66f88a6f4a9
