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
import com.praksa.team4.entities.dto.IngredientsDTO;
import com.praksa.team4.services.IngredientsServiceImpl;

@RestController
@RequestMapping(path = "project/ingredients")
@CrossOrigin(origins = "http://localhost:3000")
public class IngredientsController {

	@Autowired
	private IngredientsServiceImpl ingredientsService;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

//  TODO CEKAMO ODGOVOR : Pretraga svih sastojaka integrisana u pisanje recepta.

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return ingredientsService.getAll();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/id/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		return ingredientsService.getById(id);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name) {
		return ingredientsService.getByName(name);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewIngredient(@Valid @RequestBody IngredientsDTO newIngredient, BindingResult result) {
		return ingredientsService.addNewIngredient(newIngredient, result);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateIngredient(@PathVariable Integer id, @RequestBody IngredientsDTO updatedIngredient) {
		return ingredientsService.updateIngredient(id, updatedIngredient);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "ingredient_id/{ingredient_id}/allergen_id/{allergen_id}")
	public ResponseEntity<?> addAllergenToIngredient(@PathVariable Integer ingredient_id,
			@PathVariable Integer allergen_id) {
		return ingredientsService.addAllergenToIngredient(ingredient_id, allergen_id);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "ingredient_id/{ingredient_id}")
	public ResponseEntity<?> deleteAllergenFromIngredient(@PathVariable Integer ingredient_id) {
		return ingredientsService.deleteAllergenFromIngredient(ingredient_id);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{ingredient_id}")
	public ResponseEntity<?> deleteIngredient(@PathVariable Integer ingredient_id) {
		return ingredientsService.deleteIngredient(ingredient_id);
	}

}
