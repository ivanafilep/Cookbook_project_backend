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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.ChefRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.RESTError;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping(path = "project/chef")
public class ChefController {

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	private UserRepository userRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Chef>> getAllChefs() {
		List<Chef> chefs = (List<Chef>) chefRepository.findAll();
		return new ResponseEntity<>(chefs, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Chef> addNewChef(@Valid @RequestBody UserDTO chef) {
		Chef newChef = new Chef();

		newChef.setUsername(chef.getUsername());
		newChef.setPassword(chef.getPassword());
		newChef.setName(chef.getName());
		newChef.setLastname(chef.getLastname());
		newChef.setEmail(chef.getEmail());
		newChef.setRole("ROLE_CHEF");

		chefRepository.save(newChef);
		return new ResponseEntity<>(newChef, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateChef(@PathVariable Integer id, @RequestBody Chef updatedChef,
			Authentication authentication) {

		String email = (String) authentication.getName();
		UserEntity currentChef = userRepository.findByEmail(email);

		if (currentChef.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin " + currentChef.getName() + " " + currentChef.getLastname() + " is updating chef.");
			Chef changeChef = chefRepository.findById(id).get();

			changeChef.setUsername(updatedChef.getUsername());
			changeChef.setPassword(updatedChef.getPassword());
			changeChef.setName(updatedChef.getName());
			changeChef.setLastname(updatedChef.getLastname());
			changeChef.setEmail(updatedChef.getEmail());
			changeChef.setRole(updatedChef.getRole());
			changeChef.setRecipes(updatedChef.getRecipes());
			chefRepository.save(changeChef);

			return new ResponseEntity<>(changeChef, HttpStatus.OK);
		} else if (currentChef.getRole().equals("ROLE_CHEF")) {
			logger.info(
					"Chef" + currentChef.getName() + " " + currentChef.getLastname() + " is updating his own profile.");
			Chef chef = (Chef) currentChef;
			Chef changeChef = chefRepository.findById(id).get();

			if (chef.getId().equals(changeChef.getId())) {
				logger.info("Chef is updating his own profile.");

				changeChef.setName(updatedChef.getName());
				changeChef.setLastname(updatedChef.getLastname());
				changeChef.setUsername(updatedChef.getUsername());
				changeChef.setEmail(updatedChef.getEmail());
				changeChef.setPassword(updatedChef.getPassword());
				chefRepository.save(changeChef);

				return new ResponseEntity<>(changeChef, HttpStatus.OK);
			}
		}

		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update chef"),
				HttpStatus.UNAUTHORIZED);
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
