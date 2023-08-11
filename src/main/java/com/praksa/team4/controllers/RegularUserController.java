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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.services.RegularUserServiceImpl;
import com.praksa.team4.util.UserCustomValidator;

@RestController
@RequestMapping(path = "project/regularuser")
@CrossOrigin(origins = "http://localhost:3000")
public class RegularUserController {

	@Autowired
	private RegularUserServiceImpl regularUserService;

	@Autowired
	AllergensRepository allergensRepository;

	@Autowired
	UserCustomValidator userValidator;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return regularUserService.getAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createRegularUser(@Valid @RequestBody UserDTO newUser, BindingResult result,
			Authentication authentication) {
		return regularUserService.createRegularUser(newUser, result, authentication);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateRegularUser(@PathVariable Integer id, @RequestBody UserDTO updatedRegularUser,
			Authentication authentication) {

		return regularUserService.updateRegularUser(updatedRegularUser, id, authentication);

	}

	@Secured({"ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "regularuser_id/{regularuser_id}/allergen_id/{allergen_id}")
	public ResponseEntity<?> addAllergenToRegularUser(@PathVariable Integer regularuser_id,
			@PathVariable Integer allergen_id, Authentication authentication) {
		return regularUserService.addAllergenToRegularUser(regularuser_id, allergen_id, authentication);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "delete/regularuser_id/{regularuser_id}/allergen_id/{allergen_id}")
	public ResponseEntity<?> deleteAllergenFromRegularUser(@PathVariable Integer regularuser_id,
			@PathVariable Integer allergen_id, Authentication authentication) {
		return regularUserService.deleteAllergenFromRegularUser(regularuser_id, allergen_id, authentication);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRegularUser(@PathVariable Integer id, Authentication authentication) {
		return regularUserService.deleteRegularUser(id, authentication);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRegularUserById(@PathVariable Integer id, Authentication authentication) {
		return regularUserService.getRegularUserById(id, authentication);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRegularUserByName(@RequestParam String name) {
		return regularUserService.getRegularUserByName(name);
	}

}
