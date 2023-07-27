package com.praksa.team4.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Chef;
import com.praksa.team4.repositories.ChefRepository;

@RestController
@RequestMapping(path = "project/chef")
public class ChefController {

	@Autowired
	private ChefRepository ChefRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Chef>> getAllChefs() {
		List<Chef> chefs = (List<Chef>) ChefRepository.findAll();
		return new ResponseEntity<>(chefs, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Chef> addNewChef(@Valid @RequestBody Chef chef) {
		// Setujte id na null kako biste osigurali da se novi kuvar dodaje, a ne ažurira.
		chef.setId(null);
		Chef savedChef = ChefRepository.save(chef);
		return new ResponseEntity<>(savedChef, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<Chef> updateChef(@PathVariable Integer id, @Valid @RequestBody Chef updatedChef) {
		Chef chef = ChefRepository.findById(id).orElse(null);
		if (chef == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Postavite atribute na osnovu novih vrednosti
		chef.setUsername(updatedChef.getUsername());
		chef.setPassword(updatedChef.getPassword());
		chef.setName(updatedChef.getName());
		chef.setLastname(updatedChef.getLastname());
		chef.setEmail(updatedChef.getEmail());
		chef.setRole(updatedChef.getRole());
		chef.setRecipes(updatedChef.getRecipes());

		Chef updatedChefData = ChefRepository.save(chef);
		return new ResponseEntity<>(updatedChefData, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<String> deleteChef(@PathVariable Integer id) {
		Chef chef = ChefRepository.findById(id).orElse(null);
		if (chef == null) {
			return new ResponseEntity<>("Chef not found", HttpStatus.NOT_FOUND);
		}

		ChefRepository.delete(chef);
		return new ResponseEntity<>("Chef successfully deleted", HttpStatus.OK);
	}
}
