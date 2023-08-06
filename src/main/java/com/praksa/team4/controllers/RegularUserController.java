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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.RegularUserDTO;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.RegularUserRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.services.RegularUserServiceImpl;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@RestController
@RequestMapping(path = "project/regularuser")
public class RegularUserController {

	@Autowired
	private RegularUserRepository regularUserRepository;

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
		try {
			List<RegularUser> regularUsers = (List<RegularUser>) regularUserRepository.findAll();

			if (regularUsers.isEmpty()) {
				logger.error("No regular users found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No regular users found"), HttpStatus.NOT_FOUND);
			} else {
				ArrayList<RegularUserDTO> activeRegularUsers = new ArrayList<>();
				for(RegularUser regularUsersDB : regularUsers) {
					if (regularUsersDB.getIsActive()) {
						activeRegularUsers.add(new RegularUserDTO(regularUsersDB));
					}
				}
				logger.info("Found regular users in the database");
				return new ResponseEntity<ArrayList<RegularUserDTO>>(activeRegularUsers, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

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
			@PathVariable Integer allergen_id, Authentication authentication) {
		
		Optional<RegularUser> regularUser = regularUserRepository.findById(regularuser_id);
		
		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
	        logger.error("There is no regular user found with id " + regularuser_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + regularuser_id), HttpStatus.NOT_FOUND);
		}
		
		Optional<Allergens> allergen = allergensRepository.findById(allergen_id);

		if (allergen.isEmpty() || !allergen.get().getIsActive()) {
	        logger.error("There is no chef found with id " + allergen_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(2, "No chef found with ID " + allergen_id), HttpStatus.NOT_FOUND);
		}

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentUser.getName() + " " + currentUser.getLastname() + " is adding allergen to regular user.");
			if (!regularUser.get().getAllergens().contains(allergen.get())) {
				regularUser.get().getAllergens().add(allergen.get());
			}
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);
		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname() + " is adding allergen to his own profile.");
			RegularUser loggedInUser = (RegularUser) currentUser;
			if (loggedInUser.getId().equals(currentUser.getId()) && !regularUser.get().getAllergens().contains(allergen.get())) {
				regularUser.get().getAllergens().add(allergen.get());
			}
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);
		}
		
		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update regular user"), HttpStatus.UNAUTHORIZED);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "delete/regularuser_id/{regularuser_id}/allergen_id/{allergen_id}")
	public ResponseEntity<?> deleteAllergenFromRegularUser(@PathVariable Integer regularuser_id,
			@PathVariable Integer allergen_id, Authentication authentication) {
		
		Optional<RegularUser> regularUser = regularUserRepository.findById(regularuser_id);
		
		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
	        logger.error("There is no regular user found with id " + regularuser_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + regularuser_id), HttpStatus.NOT_FOUND);
		}
		
		Optional<Allergens> allergen = allergensRepository.findById(allergen_id);

		if (allergen.isEmpty() || !allergen.get().getIsActive()) {
	        logger.error("There is no chef found with id " + allergen_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(2, "No chef found with ID " + allergen_id), HttpStatus.NOT_FOUND);
		}

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentUser.getName() + " " + currentUser.getLastname() + " is adding allergen to regular user.");
			regularUser.get().getAllergens().remove(allergen.get());
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);
		
		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname() + " is adding allergen to his own profile.");
			RegularUser loggedInUser = (RegularUser) currentUser;
			if (loggedInUser.getId().equals(currentUser.getId()) && !regularUser.get().getAllergens().contains(allergen.get())) {
				regularUser.get().getAllergens().remove(allergen.get());
			}
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);
		}
		
		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update regular user"), HttpStatus.UNAUTHORIZED);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRegularUser(@PathVariable Integer id, Authentication authentication) {
		
		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		Optional<RegularUser> regularUser = regularUserRepository.findById(id);

		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
	        logger.error("There is no regular user found with id " + id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + id), HttpStatus.NOT_FOUND);
		} 

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentUser.getName() + " " + currentUser.getLastname() + " is deleting users profile.");
			regularUser.get().setIsActive(false);
			regularUser.get().setMyCookBook(null);
			regularUserRepository.save(regularUser.get());
			logger.info("Deleting regular user from the database");
			return new ResponseEntity<>("Regular user with ID " + id + " has been successfully deleted.", HttpStatus.OK);

		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname() + " is deleting his own profile.");
			RegularUser loggedInUser = (RegularUser) currentUser;
			if (loggedInUser.getId().equals(currentUser.getId())) {
				regularUser.get().setIsActive(false);
				regularUser.get().setMyCookBook(null);
				regularUserRepository.save(regularUser.get());
				logger.info("Deleting regular user from the database");
				return new ResponseEntity<>("Regular user with ID " + id + " has been successfully deleted.", HttpStatus.OK);
			}
		}
		
		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update regular user"), HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRegularUserById(@PathVariable Integer id) {
		
		Optional<RegularUser> regularUser = regularUserRepository.findById(id);
		
		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
	        logger.error("There is no regular user found with id " + id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + id), HttpStatus.NOT_FOUND);
		} else {
			logger.info("Regular user found in the database: " + regularUser.get().getName() + regularUser.get().getLastname() + ".");
			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRegularUserByName(@RequestParam String name) {
		Optional<RegularUser> regularUser = regularUserRepository.findByName(name);

		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
			logger.error("No regular user found in the database with " + name + ".");
			return new ResponseEntity<RESTError>(new RESTError(1, "Regular user not found"), HttpStatus.NOT_FOUND);
		} else {
			logger.info("Regular user found in the database: " + name + ".");
			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.OK);
		}
	}
	
}
