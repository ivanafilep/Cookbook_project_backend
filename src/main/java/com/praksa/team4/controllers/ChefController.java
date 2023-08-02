package com.praksa.team4.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.ChefRepository;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping(path = "project/chef")
public class ChefController {

	@Autowired
	private ChefRepository chefRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Chef>> getAllChefs() {
		List<Chef> chefs = (List<Chef>) chefRepository.findAll();
		return new ResponseEntity<>(chefs, HttpStatus.OK);
	}

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

	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<Chef> updateChef(@PathVariable Integer id, @Valid @RequestBody Chef updatedChef) {
		Chef chef = chefRepository.findById(id).orElse(null);
		if (chef == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		chef.setUsername(updatedChef.getUsername());
		chef.setPassword(updatedChef.getPassword());
		chef.setName(updatedChef.getName());
		chef.setLastname(updatedChef.getLastname());
		chef.setEmail(updatedChef.getEmail());
		chef.setRole(updatedChef.getRole());
		chef.setRecipes(updatedChef.getRecipes());

		chefRepository.save(chef);
		return new ResponseEntity<>(chef, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<String> deleteChef(@PathVariable Integer id) {
		Chef chef = chefRepository.findById(id).orElse(null);
		if (chef == null) {
			return new ResponseEntity<>("Chef not found", HttpStatus.NOT_FOUND);
		}

		chefRepository.delete(chef);
		return new ResponseEntity<>("Chef successfully deleted", HttpStatus.OK);
	}
}
