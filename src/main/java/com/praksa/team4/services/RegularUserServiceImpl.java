package com.praksa.team4.services;

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

import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.RegularUserDTO;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.CookBookRepository;
import com.praksa.team4.repositories.RegularUserRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@Service
public class RegularUserServiceImpl implements RegularUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RegularUserRepository regularUserRepository;

	@Autowired
	CookBookRepository cookBookRepository;

	@Autowired
	UserCustomValidator userValidator;
	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> createRegularUser(@Valid UserDTO newUser, BindingResult result, Authentication authentication) {
		
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

		//TODO provera != da l radi posao
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
		newRegularUser.setRole("ROLE_REGULAR_USER");
		logger.info("Setting users role.");

		regularUserRepository.save(newRegularUser);
		logger.info("Saving student to the database");

		MyCookBook myCookBook = new MyCookBook();
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
			return new ResponseEntity<RESTError>(new RESTError(1, "No regular user found with ID " + id), HttpStatus.NOT_FOUND);
		}
		
		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentUser.getName() + " " + currentUser.getLastname() + " is updating regular user.");
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


}
