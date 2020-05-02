package com.cts.managerService.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.cts.managerService.entityClass.RatingData;



@Repository
public class CustomRatingrepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Logger logger = LoggerFactory.getLogger(CustomRatingrepository.class);
	
	public List<RatingData> getRatingsByOrder(Integer employeeId){
		logger.info("EMPLOYEE ID ======= >>>>>>>>>    "+employeeId);
		TypedQuery<RatingData> query = this.entityManager.createQuery("select r from RatingData r group by :employeeId order by avg(rating) desc", RatingData.class);
		query.setParameter("employeeId", employeeId);
		List<RatingData> data = query.getResultList();
		return data;
		
	}
}
