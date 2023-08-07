package com.praksa.team4.services;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.RegularUserDTO;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.CookBookRepository;
import com.praksa.team4.repositories.RegularUserRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@Service
public class RegularUserServiceImpl implements RegularUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RegularUserRepository regularUserRepository;

	@Autowired
	private CookBookRepository cookBookRepository;

	@Autowired
	private AllergensRepository allergensRepository;

	@Autowired
	UserCustomValidator userValidator;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> getAll() {
		try {
			List<RegularUser> regularUsers = (List<RegularUser>) regularUserRepository.findAll();

			if (regularUsers.isEmpty()) {
				logger.error("No regular users found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No regular users found"), HttpStatus.NOT_FOUND);
			} else {
				ArrayList<RegularUserDTO> activeRegularUsers = new ArrayList<>();
				for (RegularUser regularUsersDB : regularUsers) {
					if (regularUsersDB.getIsActive()) {
						activeRegularUsers.add(new RegularUserDTO(regularUsersDB));
					}
				}
				logger.info("Found regular users in the database");
				return new ResponseEntity<ArrayList<RegularUserDTO>>(activeRegularUsers, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> createRegularUser(@Valid UserDTO newUser, BindingResult result,
			Authentication authentication) {

		if (result.hasErrors()) {
			logger.error("Sent incorrect parameters.");
			return new ResponseEntity<>(ErrorMessageHelper.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		} else {
			logger.info("Validating if the users password matches the confirming password");
			userValidator.validate(newUser, result);
			if (result.hasErrors()) {
				logger.error("Validation errors detected.");
				return new ResponseEntity<>(result.getFieldError(), HttpStatus.BAD_REQUEST);
			}
		}

		UserEntity existingUserWithEmail = userRepository.findByEmail(newUser.getEmail());
		logger.info("Finding out whether there's a user with the same email.");

		UserEntity existingUserWithUsername = userRepository.findByUsername(newUser.getUsername());
		logger.info("Finding out whether there's a user with the same username.");

		// TODO provera != da l radi posao
		if (existingUserWithEmail != null) {
			logger.error("There is a user with the same email.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Email already exists"), HttpStatus.CONFLICT);
		}

		if (existingUserWithUsername != null) {
			logger.error("There is a user with the same username.");
			return new ResponseEntity<RESTError>(new RESTError(2, "Username already exists"), HttpStatus.CONFLICT);
		}

		RegularUser newRegularUser = new RegularUser();

		newRegularUser.setName(newUser.getName());
		newRegularUser.setLastname(newUser.getLastname());
		newRegularUser.setUsername(newUser.getUsername());
		newRegularUser.setEmail(newUser.getEmail());
		newRegularUser.setPassword(newUser.getPassword());
		newRegularUser.setIsActive(true);
		newRegularUser.setRole("ROLE_REGULAR_USER");
		newRegularUser.setAllergens(new ArrayList<Allergens>());
		logger.info("Setting users role.");

		regularUserRepository.save(newRegularUser);
		logger.info("Saving regular user to the database");

		MyCookBook myCookBook = new MyCookBook();
		myCookBook.setIsActive(true);
		myCookBook.setRegularUser(newRegularUser);
		logger.info("My cookbook" + myCookBook);
		newRegularUser.setMyCookBook(myCookBook);

		cookBookRepository.save(myCookBook);
		regularUserRepository.save(newRegularUser);
		logger.info("Saving cookbook to the regular user");

		return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(newRegularUser), HttpStatus.CREATED);

	}

	public ResponseEntity<?> updateRegularUser(UserDTO updatedRegularUser, Integer id, Authentication authentication) {

		Optional<RegularUser> changeUser = regularUserRepository.findById(id);

		if (changeUser.isEmpty() || !changeUser.get().getIsActive()) {
			logger.error("There is no regular user found with id" + id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + id),
					HttpStatus.NOT_FOUND);
		}

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info(
					"Admin " + currentUser.getName() + " " + currentUser.getLastname() + " is updating regular user.");
			changeUser.get().setName(updatedRegularUser.getName());
			changeUser.get().setLastname(updatedRegularUser.getLastname());
			changeUser.get().setUsername(updatedRegularUser.getUsername());
			changeUser.get().setEmail(updatedRegularUser.getEmail());
			changeUser.get().setPassword(updatedRegularUser.getPassword());
			regularUserRepository.save(changeUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(changeUser.get()), HttpStatus.OK);
		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname()
					+ " is updating his own profile.");
			RegularUser regularUser = (RegularUser) currentUser;

			if (regularUser.getId().equals(changeUser.get().getId())) {
				logger.info("Regular user is updating his own profile.");

				changeUser.get().setName(updatedRegularUser.getName());
				changeUser.get().setLastname(updatedRegularUser.getLastname());
				changeUser.get().setUsername(updatedRegularUser.getUsername());
				changeUser.get().setEmail(updatedRegularUser.getEmail());
				changeUser.get().setPassword(updatedRegularUser.getPassword());
				regularUserRepository.save(changeUser.get());

				return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(changeUser.get()), HttpStatus.OK);
			}
		}

		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update regular user"),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> addAllergenToRegularUser(@PathVariable Integer regularuser_id,
			@PathVariable Integer allergen_id, Authentication authentication) {

		Optional<RegularUser> regularUser = regularUserRepository.findById(regularuser_id);

		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
			logger.error("There is no regular user found with id " + regularuser_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + regularuser_id),
					HttpStatus.NOT_FOUND);
		}

		Optional<Allergens> allergen = allergensRepository.findById(allergen_id);

		if (allergen.isEmpty() || !allergen.get().getIsActive()) {
			logger.error("There is no chef found with id " + allergen_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(2, "No chef found with ID " + allergen_id),
					HttpStatus.NOT_FOUND);
		}

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentUser.getName() + " " + currentUser.getLastname()
					+ " is adding allergen to regular user.");
			if (!regularUser.get().getAllergens().contains(allergen.get())) {
				regularUser.get().getAllergens().add(allergen.get());
			}
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);
		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname()
					+ " is adding allergen to his own profile.");
			RegularUser loggedInUser = (RegularUser) currentUser;
			if (loggedInUser.getId().equals(currentUser.getId())
					&& !regularUser.get().getAllergens().contains(allergen.get())) {
				regularUser.get().getAllergens().add(allergen.get());
			}
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);
		}

		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update regular user"),
				HttpStatus.UNAUTHORIZED);

	}

	public ResponseEntity<?> deleteAllergenFromRegularUser(@PathVariable Integer regularuser_id,
			@PathVariable Integer allergen_id, Authentication authentication) {

		Optional<RegularUser> regularUser = regularUserRepository.findById(regularuser_id);

		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
			logger.error("There is no regular user found with id " + regularuser_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + regularuser_id),
					HttpStatus.NOT_FOUND);
		}

		Optional<Allergens> allergen = allergensRepository.findById(allergen_id);

		if (allergen.isEmpty() || !allergen.get().getIsActive()) {
			logger.error("There is no chef found with id " + allergen_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(2, "No chef found with ID " + allergen_id),
					HttpStatus.NOT_FOUND);
		}

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentUser.getName() + " " + currentUser.getLastname()
					+ " is adding allergen to regular user.");
			regularUser.get().getAllergens().remove(allergen.get());
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);

		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname()
					+ " is adding allergen to his own profile.");
			RegularUser loggedInUser = (RegularUser) currentUser;
			if (loggedInUser.getId().equals(currentUser.getId())
					&& !regularUser.get().getAllergens().contains(allergen.get())) {
				regularUser.get().getAllergens().remove(allergen.get());
			}
			regularUserRepository.save(regularUser.get());

			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.CREATED);
		}

		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update regular user"),
				HttpStatus.UNAUTHORIZED);

	}

	public ResponseEntity<?> deleteRegularUser(@PathVariable Integer id, Authentication authentication) {

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		Optional<RegularUser> regularUser = regularUserRepository.findById(id);

		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
			logger.error("There is no regular user found with id " + id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + id),
					HttpStatus.NOT_FOUND);
		}

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info(
					"Admin " + currentUser.getName() + " " + currentUser.getLastname() + " is deleting users profile.");
			regularUser.get().setIsActive(false);
			regularUser.get().setMyCookBook(null);
			regularUserRepository.save(regularUser.get());
			logger.info("Deleting regular user from the database");
			return new ResponseEntity<>("Regular user with ID " + id + " has been successfully deleted.",
					HttpStatus.OK);

		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname()
					+ " is deleting his own profile.");
			RegularUser loggedInUser = (RegularUser) currentUser;
			if (loggedInUser.getId().equals(currentUser.getId())) {
				regularUser.get().setIsActive(false);
				regularUser.get().setMyCookBook(null);
				regularUserRepository.save(regularUser.get());
				logger.info("Deleting regular user from the database");
				return new ResponseEntity<>("Regular user with ID " + id + " has been successfully deleted.",
						HttpStatus.OK);
			}
		}

		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update regular user"),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> getRegularUserById(@PathVariable Integer id) {

		Optional<RegularUser> regularUser = regularUserRepository.findById(id);

		if (regularUser.isEmpty() || !regularUser.get().getIsActive()) {
			logger.error("There is no regular user found with id " + id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + id),
					HttpStatus.NOT_FOUND);
		} else {
			logger.info("Regular user found in the database: " + regularUser.get().getName()
					+ regularUser.get().getLastname() + ".");
			return new ResponseEntity<RegularUserDTO>(new RegularUserDTO(regularUser.get()), HttpStatus.OK);
		}
	}

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
