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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.CookBookRepository;
import com.praksa.team4.repositories.RegularUserRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.RESTError;

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
	AllergensRepository allergensRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<Iterable<RegularUser>>(regularUserRepository.findAll(), HttpStatus.OK);
	}

	// TODO SignUp? ne treba secured, jer svima treba da bude dostupno? 
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewRegularUser(@Valid @RequestBody UserDTO newUser) {
		
		RegularUser newRegularUser = new RegularUser();

		newRegularUser.setName(newUser.getName());
		newRegularUser.setLastname(newUser.getLastname());
		newRegularUser.setUsername(newUser.getUsername());
		newRegularUser.setEmail(newUser.getEmail());
		newRegularUser.setPassword(newUser.getPassword());
		newRegularUser.setRole("ROLE_REGULAR_USER");

		regularUserRepository.save(newRegularUser);

		MyCookBook myCookBook = new MyCookBook();
		myCookBook.setRegularUser(newRegularUser);
		logger.info("My cookbook" + myCookBook);
		newRegularUser.setMyCookBook(myCookBook);

		cookBookRepository.save(myCookBook);
		regularUserRepository.save(newRegularUser);
		return new ResponseEntity<>(newRegularUser, HttpStatus.CREATED);
	}

	@Secured({"ROLE_ADMIN", "ROLE_REGULAR_USER"})
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateRegularUser(@PathVariable Integer id, @RequestBody UserDTO updatedRegularUser,
		Authentication authentication) {
		
		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentUser.getName() + " " + currentUser.getLastname() + " is updating regular user.");
			RegularUser changeUser = regularUserRepository.findById(id).get();
			changeUser.setName(updatedRegularUser.getName());
			changeUser.setLastname(updatedRegularUser.getLastname());
			changeUser.setUsername(updatedRegularUser.getUsername());
			changeUser.setEmail(updatedRegularUser.getEmail());
			changeUser.setPassword(updatedRegularUser.getPassword());
			regularUserRepository.save(changeUser);
			
			return new ResponseEntity<>(changeUser, HttpStatus.OK);
		}
		else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname() + " is updating his own profile.");
			RegularUser regularUser = (RegularUser) currentUser;
			RegularUser changeUser = regularUserRepository.findById(id).get();
			
			if (regularUser.getId().equals(changeUser.getId())) {
				logger.info("Regular user is updating his own profile.");

				changeUser.setName(updatedRegularUser.getName());
				changeUser.setLastname(updatedRegularUser.getLastname());
				changeUser.setUsername(updatedRegularUser.getUsername());
				changeUser.setEmail(updatedRegularUser.getEmail());
				changeUser.setPassword(updatedRegularUser.getPassword());
				regularUserRepository.save(changeUser);
				
				return new ResponseEntity<>(changeUser, HttpStatus.OK);
			}
		}
	
		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update regular user"), HttpStatus.UNAUTHORIZED);
	}

	@Secured({"ROLE_ADMIN", "ROLE_REGULAR_USER"})
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

	@Secured({"ROLE_ADMIN", "ROLE_REGULAR_USER"})
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
		if (regularUser.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			regularUserRepository.delete(regularUser.get());
			return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRegularUserById(@PathVariable Integer id) {
		Optional<RegularUser> regularUser = regularUserRepository.findById(id);

		if (!regularUser.isPresent()) {
			return new ResponseEntity<>("Regular user not found!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(regularUser.get(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRegularUserByName(@RequestParam String name) {
		Optional<RegularUser> regularUser = regularUserRepository.findByName(name);

		if (!regularUser.isPresent()) {
			return new ResponseEntity<>("Regular user not found!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(regularUser.get(), HttpStatus.OK);
	}
}
