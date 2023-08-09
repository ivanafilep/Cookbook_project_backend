package com.praksa.team4.controllers;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.services.ChefServiceImpl;
import com.praksa.team4.util.UserCustomValidator;

@RestController
@RequestMapping(path = "project/chef")
@CrossOrigin(origins = "http://localhost:3000")
public class ChefController {


	@Autowired
	UserCustomValidator userValidator;

	@Autowired
	private ChefServiceImpl chefService;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllChefs() {
		return chefService.getAllChefs();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createChef(@Valid @RequestBody UserDTO chef, BindingResult result) {
		return chefService.createChef(chef, result);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateChef(@PathVariable Integer id, @RequestBody Chef updatedChef, BindingResult result,
			Authentication authentication) {
		return chefService.updateChef(updatedChef, result, id, authentication);

	}
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getChefById(@PathVariable Integer id) {
		return chefService.getChefById(id);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteChef(@PathVariable Integer id) {
		return chefService.deleteChef(id);
	}
}
