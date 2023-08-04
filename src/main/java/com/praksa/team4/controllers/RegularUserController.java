package com.praksa.team4.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.CookBookRepository;
import com.praksa.team4.repositories.RegularUserRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.services.RegularUserServiceImpl;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@RestController
@RequestMapping(path = "project/regularuser")
public class RegularUserController {

	@Autowired
	private RegularUserRepository regularUserRepository;

	@Autowired
	private CookBookRepository cookBookRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RegularUserServiceImpl regularUserServiceImpl;
	
	@Autowired
	AllergensRepository allergensRepository;

	@Autowired
	UserCustomValidator userValidator;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		List<RegularUser> regularUsers = (List<RegularUser>) regularUserRepository.findAll();

		if (regularUsers.isEmpty()) {
			logger.error("No regular users found in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular users found"), HttpStatus.NOT_FOUND);
		} else {
			logger.info("Found regular users in the database");
			return new ResponseEntity<Iterable<RegularUser>>(regularUserRepository.findAll(), HttpStatus.OK);
		}
	}

	// TODO SignUp? ne treba secured, jer svima treba da bude dostupno?
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createRegularUser(@Valid @RequestBody UserDTO newUser, BindingResult result,
			Authentication authentication) {
		return regularUserServiceImpl.createRegularUser(newUser, result, authentication);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateRegularUser(@PathVariable Integer id, @RequestBody UserDTO updatedRegularUser,
			Authentication authentication) {
		
		return regularUserServiceImpl.updateRegularUser(updatedRegularUser, id, authentication);

		}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "regularuser_id/{regularuser_id}/allergen_id/{allergen_id}")
	public ResponseEntity<?> addAllergenToRegularUser(@PathVariable Integer regularuser_id,
			@PathVariable Integer allergen_id) {
		RegularUser regularUser = regularUserRepository.findById(regularuser_id).get();
		Allergens allergen = allergensRepository.findById(allergen_id).get();

		regularUser.getAllergens().add(allergen);

		// TODO do not duplicate allergen

		regularUserRepository.save(regularUser);

		return new ResponseEntity<>(regularUser, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "delete/regularuser_id/{regularuser_id}/allergen_id/{allergen_id}")
	public ResponseEntity<?> deleteAllergenFromRegularUser(@PathVariable Integer regularuser_id,
			@PathVariable Integer allergen_id) {
		RegularUser regularUser = regularUserRepository.findById(regularuser_id).get();
		Allergens allergen = allergensRepository.findById(allergen_id).get();

		regularUser.getAllergens().remove(allergen);

		regularUserRepository.save(regularUser);

		return new ResponseEntity<>(regularUser, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRegularUser(@PathVariable Integer id) {
		Optional<RegularUser> regularUser = regularUserRepository.findById(id);

		// TODO delete also MyCookBook

		if (!regularUser.isPresent()) {
			logger.error("There is no regular user found with " + id);
			return new ResponseEntity<RESTError>(new RESTError(1, "Regular User found"), HttpStatus.NOT_FOUND);
		} else {
			regularUserRepository.delete(regularUser.get());
			logger.info("Deleting regular user from the database");
			return new ResponseEntity<>("Regular user with ID " + id + " has been successfully deleted.",
					HttpStatus.OK);

		}

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRegularUserById(@PathVariable Integer id) {
		Optional<RegularUser> regularUser = regularUserRepository.findById(id);

		if (regularUser.isPresent()) {
			logger.info("Regular user found in the database: " + regularUser.get().getName()
					+ regularUser.get().getLastname() + ".");
			return new ResponseEntity<RegularUser>(regularUser.get(), HttpStatus.OK);
		} else {
			logger.error("No regular user found in the database with: " + id + ".");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found"), HttpStatus.NOT_FOUND);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRegularUserByName(@RequestParam String name) {
		Optional<RegularUser> regularUser = regularUserRepository.findByName(name);

		if (regularUser.isPresent()) {
			logger.info("Regular user found in the database: " + name + ".");
			return new ResponseEntity<RegularUser>(regularUser.get(), HttpStatus.OK);
		} else {
			logger.error("No regular user found in the database with " + name + ".");
			return new ResponseEntity<RESTError>(new RESTError(1, "Regular user not found"), HttpStatus.NOT_FOUND);
		}
	}
}
