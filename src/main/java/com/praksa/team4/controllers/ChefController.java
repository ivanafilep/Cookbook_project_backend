package com.praksa.team4.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.dto.ChefDTO;
import com.praksa.team4.entities.dto.MyCookBookDTO;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.ChefRepository;
import com.praksa.team4.services.ChefServiceImpl;
import com.praksa.team4.util.RESTError;
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
	public ResponseEntity<?> getAllChefs() {
		try {
			List<Chef> chefs = (List<Chef>) chefRepository.findAll();

			if (chefs.isEmpty()) {
		        logger.error("No chef found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No chefs found in the database"), HttpStatus.NOT_FOUND);
			} else {
				ArrayList<ChefDTO> activeChefs = new ArrayList<>();
				for(Chef chefDB : chefs) {
					if (chefDB.getIsActive()) {
						activeChefs.add(new ChefDTO(chefDB));
					}
				}
		        logger.info("Found chefs in the database");
				return new ResponseEntity<ArrayList<ChefDTO>>(activeChefs, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewChef(@Valid @RequestBody UserDTO chef, BindingResult result) {
		return chefServiceImpl.createChef(chef, result);

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
		
		Optional<Chef> chef = chefRepository.findById(id);
		
		if (chef.isEmpty() || !chef.get().getIsActive()) {
			return new ResponseEntity<>("Chef not found in the database", HttpStatus.NOT_FOUND);
		}

		chef.get().setIsActive(false);
		chefRepository.save(chef.get());
		return new ResponseEntity<>("Chef successfully deleted", HttpStatus.OK);
	}
}
