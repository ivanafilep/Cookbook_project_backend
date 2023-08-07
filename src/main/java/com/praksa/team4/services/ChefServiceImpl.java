package com.praksa.team4.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.ChefDTO;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.ChefRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@Service
public class ChefServiceImpl implements ChefService {

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserCustomValidator userValidator;
	
    @Autowired
    private PasswordEncoder passwordEncoder;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> getAllChefs() {
		try {
			List<Chef> chefs = (List<Chef>) chefRepository.findAll();

			if (chefs.isEmpty()) {
				logger.error("No chef found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No chefs found in the database"),
						HttpStatus.NOT_FOUND);
			} else {
				ArrayList<ChefDTO> activeChefs = new ArrayList<>();
				for (Chef chefDB : chefs) {
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

	public ResponseEntity<?> createChef(UserDTO chef, BindingResult result) {

		if (result.hasErrors()) {
			logger.error("Sent incorrect parameters.");
			return new ResponseEntity<RESTError>(new RESTError(1, ErrorMessageHelper.createErrorMessage(result)),
					HttpStatus.BAD_REQUEST);
		} else {
			logger.info("Validating if the users password matches the confirming password");
			userValidator.validate(chef, result);
			if (result.hasErrors()) {
				logger.error("Validation errors detected.");
				return new ResponseEntity<>(result.getFieldError(), HttpStatus.BAD_REQUEST);
			}
		}

		UserEntity existingUserWithEmail = userRepository.findByEmail(chef.getEmail());
		logger.info("Finding out whether there's a user with the same email.");

		UserEntity existingUserWithUsername = userRepository.findByUsername(chef.getUsername());
		logger.info("Finding out whether there's a user with the same username.");

		// TODO proveriti ove 57 i 62 da li izbacuju gresku dobru
		if (existingUserWithEmail != null) {
			logger.error("There is a user with the same email.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Email already exists"), HttpStatus.CONFLICT);
		}

		if (existingUserWithUsername != null) {
			logger.error("There is a user with the same username.");
			return new ResponseEntity<RESTError>(new RESTError(2, "Username already exists"), HttpStatus.CONFLICT);
		}

		Chef newChef = new Chef();

		newChef.setUsername(chef.getUsername());
		newChef.setPassword(passwordEncoder.encode(chef.getPassword()));
		newChef.setName(chef.getName());
		newChef.setLastname(chef.getLastname());
		newChef.setEmail(chef.getEmail());
		newChef.setRole("ROLE_CHEF");
		newChef.setIsActive(true);
		logger.info("Setting chef role.");

		chefRepository.save(newChef);
		logger.info("Saving chef to the database");

		return new ResponseEntity<ChefDTO>(new ChefDTO(newChef), HttpStatus.CREATED);

	}

	public ResponseEntity<?> updateChef(Chef updatedChef, BindingResult result, Integer id,
			Authentication authentication) {

		Optional<Chef> changeChef = chefRepository.findById(id);

		if (changeChef.isEmpty() || !changeChef.get().getIsActive()) {
			logger.error("There is no chef found with id" + id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No chef found with ID " + id), HttpStatus.NOT_FOUND);
		}

		String email = (String) authentication.getName();
		UserEntity currentChef = userRepository.findByEmail(email);

		if (currentChef.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentChef.getName() + " " + currentChef.getLastname() + " is updating chef.");

			changeChef.get().setUsername(updatedChef.getUsername());
			changeChef.get().setPassword(updatedChef.getPassword());
			changeChef.get().setName(updatedChef.getName());
			changeChef.get().setLastname(updatedChef.getLastname());
			changeChef.get().setEmail(updatedChef.getEmail());
			changeChef.get().setRole(updatedChef.getRole());
			changeChef.get().setRecipes(updatedChef.getRecipes());
			chefRepository.save(changeChef.get());

			return new ResponseEntity<ChefDTO>(new ChefDTO(changeChef.get()), HttpStatus.OK);
		} else if (currentChef.getRole().equals("ROLE_CHEF")) {
			logger.info(
					"Chef" + currentChef.getName() + " " + currentChef.getLastname() + " is updating his own profile.");
			Chef chef = (Chef) currentChef;

			if (chef.getId().equals(changeChef.get().getId())) {
				logger.info("Chef is updating his own profile.");

				changeChef.get().setName(updatedChef.getName());
				changeChef.get().setLastname(updatedChef.getLastname());
				changeChef.get().setUsername(updatedChef.getUsername());
				changeChef.get().setEmail(updatedChef.getEmail());
				changeChef.get().setPassword(updatedChef.getPassword());
				chefRepository.save(changeChef.get());

				return new ResponseEntity<ChefDTO>(new ChefDTO(changeChef.get()), HttpStatus.OK);
			}
		}

		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update chef"),
				HttpStatus.UNAUTHORIZED);

	}

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
