package com.cts.employeeService.controller;

import java.util.List;

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

import com.cts.employeeService.entity.Delivarable;
import com.cts.employeeService.entity.User;
import com.cts.employeeService.exceptions.customExceptions.ErrorResponse;
import com.cts.employeeService.exceptions.customExceptions.IdNotFoundException;
import com.cts.employeeService.model.DelivarableModel;
import com.cts.employeeService.model.DelivarableStatus;
import com.cts.employeeService.service.EmployeeService;

import io.swagger.annotations.Api;

@RestController
@Api
@RefreshScope
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	//This method saves a Delivarable
	@PostMapping("/saveDelivarable")
	public Delivarable saveDelivarable(@RequestBody DelivarableModel delivarableModel ) {
		
		if(StringUtils.isBlank(delivarableModel.getProjectname())) {
			throw new ValidationException("Project name can not be blank");
		}
		else {
		return this.employeeService.saveDelivarable(delivarableModel);
		}
	}
	
	//It update the rating status when manger rates the delivarable
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
	
	//It updates the review status when employee provides the review
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
	
	//Retrieve all the employee ids of a manager
	@GetMapping("/employeeIds/{managerId}")
	public ResponseEntity<List<Integer>> getEmployeeIdsBymanagerId(@PathVariable Integer managerId){
		try {
			return this.employeeService.getEmployeeIds(managerId);
		}catch(Exception e) {
			throw new IdNotFoundException("No employee with maganer id "+managerId+" not found");		
		}
	}
	
	//Retrieves the list of delivarables saved by employee Id
	@GetMapping("/getDelivarables/{employeeId}")
	public ResponseEntity<List<Delivarable>> getDelivarablesByEmployeeId(@PathVariable Integer employeeId){
		try {
		return this.employeeService.getDelivarablesByEmployeeId(employeeId);
		}catch(Exception e) {
				throw new IdNotFoundException("Employee With id "+employeeId+" not found");		
		}
	}
	
	// retrieve the user details by userId And ManagerId
	@GetMapping("/userDetails/{employeeId}/{managerId}")
	public ResponseEntity<User> getDetailsByEmployeeIdAndBymanagerId(@PathVariable Integer employeeId,@PathVariable Integer managerId){
		return this.employeeService.getUserByManagerId(employeeId, managerId);
	}
	
	@ExceptionHandler 
	public ResponseEntity<ErrorResponse> idNotFoundHandler(IdNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(ex.getMessage(), 
															  HttpStatus.NOT_FOUND.value(), 
															  System.currentTimeMillis());
	
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
		
	}
	

	
}


