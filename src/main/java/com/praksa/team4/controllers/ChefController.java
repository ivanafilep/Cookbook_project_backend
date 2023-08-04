package com.praksa.team4.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.ChefRepository;
import com.praksa.team4.services.ChefServiceImpl;
import com.praksa.team4.util.UserCustomValidator;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping(path = "project/chef")
public class ChefController {

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	UserCustomValidator userValidator;

	@Autowired
	private ChefServiceImpl chefServiceImpl;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Chef>> getAllChefs() {
		List<Chef> chefs = (List<Chef>) chefRepository.findAll();
		return new ResponseEntity<>(chefs, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewChef(@Valid @RequestBody UserDTO chef, BindingResult result,
			Authentication authentication) {
		return chefServiceImpl.createChef(chef, result, authentication);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateChef(@PathVariable Integer id, @RequestBody Chef updatedChef, BindingResult result,
			Authentication authentication) {
		return chefServiceImpl.updateChef(updatedChef, result, id, authentication);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteChef(@PathVariable Integer id) {
		Chef chef = chefRepository.findById(id).orElse(null);
		if (chef == null) {
			return new ResponseEntity<>("Chef not found", HttpStatus.NOT_FOUND);
		}

		chefRepository.delete(chef);
		return new ResponseEntity<>("Chef successfully deleted", HttpStatus.OK);
	}
}
