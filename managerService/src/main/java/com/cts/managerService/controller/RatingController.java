package com.cts.managerService.controller;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cts.managerService.entityClass.RatingData;
import com.cts.managerService.exceptions.ResourceNotFoundException;
import com.cts.managerService.modelClass.RatingDataModel;
import com.cts.managerService.modelClass.ReviewDataModel;
import com.cts.managerService.repository.RatingRepository;
import com.cts.managerService.service.RatingService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;


@ControllerAdvice
@RestController
@Api
@RefreshScope
public class RatingController {
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private RatingRepository ratingRepo;

	
	@Value("${msg:Config Server is not working. Please check...}")
    private String msg;
	
	@HystrixCommand(fallbackMethod = "fallBackGetMsg" , commandProperties = {
		      @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")})
    @GetMapping("/msg")
    public String getMsg() {
        return this.msg;
    }
    public String fallBackGetMsg() {
        return "fall back method";
    }
	
    @PostMapping("/saveRating")
	public void saveRating(@RequestBody RatingDataModel dataModel) throws Exception {
    	if(dataModel.getRating()==null) {
			 throw new Exception("Rating can not be empty or null");	
		}
		else {
			this.ratingService.saveRating(dataModel);
			
		}
	}
		
	
	@PutMapping("/updateReview")
	public void updateReview(@RequestBody ReviewDataModel dataModel) throws Exception  {
		if(StringUtils.isBlank(dataModel.getReview())) {
			 throw new Exception("Review can not be empty or null");	
		}
		else {
		this.ratingService.updateRating(dataModel);
		}
	}
	
	@GetMapping("/getRating/{delivarableId}")
	public ResponseEntity<RatingData> getRatingByDelivarableId(@PathVariable Integer delivarableId){
		
		if(this.ratingRepo.findBydelivarableId(delivarableId)!=null) {
			return this.ratingService.getBydelivarableId(delivarableId);
				
		}else {
			throw new ResourceNotFoundException("Given Delivarable id '"+delivarableId+"' is not avilable");
		}
	}
	@GetMapping("/getByOrder")
	public ResponseEntity<List<Integer>> getRatingsByOrder() throws Exception{
		List<RatingData> r = this.ratingRepo.findAll();
		if(r.isEmpty()) {
			throw new Exception("No data in db");	
		}else {
			return this.ratingService.getRatingsByOrder();
		}
	}
	
	
}
