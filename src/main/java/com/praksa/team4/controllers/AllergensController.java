package com.praksa.team4.controllers;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.dto.AllergensDTO;
import com.praksa.team4.services.AllergensServiceImpl;

@RestController
@RequestMapping(path = "project/allergens")
@CrossOrigin(origins = "http://localhost:3000")
public class AllergensController {


	@Autowired
	private AllergensServiceImpl allergensService;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return allergensService.getAll();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		return allergensService.getById(id);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name) {
		return allergensService.getByName(name);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAllergen(@Valid @RequestBody AllergensDTO allergen, BindingResult result) {
		return allergensService.addNewAllergen(allergen, result);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateAllergen(@PathVariable Integer id, @RequestBody AllergensDTO updatedAllergen) {
		return allergensService.updateAllergen(id, updatedAllergen);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteAllergen(@PathVariable Integer id) {
		return allergensService.deleteAllergen(id);
	}
}
