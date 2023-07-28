package com.praksa.team4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.services.RegularUserAllergensServiceImpl;

@RestController
@RequestMapping(path = "project/regularUserAllergens")
public class RegularUserAllergensController {

	@Autowired
	private RegularUserAllergensServiceImpl regularUserAllergensServiceImpl;

	//TODO get
	
	@RequestMapping(method = RequestMethod.POST, path = "/regularUser/{regularUserId}/allergens/{allergensId}")
	public ResponseEntity<?> addAllergensToRegularUser(@PathVariable Integer regularUserId,
			@PathVariable Integer allergensId) {
		return regularUserAllergensServiceImpl.addAllergensToRegularUser(regularUserId, allergensId);
	}
	
	//TODO put
	
	//TODO delete
}
