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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.dto.IngredientsDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;

@RestController
@RequestMapping(path = "project/ingredients")
@CrossOrigin(origins = "http://localhost:3000")
public class IngredientsController {

	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	private AllergensRepository allergensRepository;
	
	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

//  TODO CEKAMO ODGOVOR : Pretraga svih sastojaka integrisana u pisanje recepta.

	//TODO svi bi trebali da vide - da li je to public view samo ili sta
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			List<Ingredients> ingredients = (List<Ingredients>) ingredientsRepository.findAll();

			if (ingredients.isEmpty()) {
		        logger.error("No ingredients found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No ingredients found"), HttpStatus.NOT_FOUND);
			} else {
		        logger.info("Found ingredients in the database");
				return new ResponseEntity<Iterable<Ingredients>>(ingredientsRepository.findAll(), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		Optional<Ingredients> ingredient = ingredientsRepository.findById(id);

		if (ingredient.isPresent()) {
			logger.info("Found ingredient in the database");
			return new ResponseEntity<Ingredients>(ingredient.get(), HttpStatus.OK);
		} else {
			logger.error("No ingredient found in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1,"No ingredient found with ID " + id), HttpStatus.NOT_FOUND);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewIngredient(@Valid @RequestBody IngredientsDTO newIngredient, BindingResult result) {
		
		if (result.hasErrors()) {
	        logger.error("Sent incorrect parameters.");
			return new ResponseEntity<RESTError>(new RESTError(1,ErrorMessageHelper.createErrorMessage(result)), HttpStatus.BAD_REQUEST);
		}
		
		Ingredients existingIngredient = ingredientsRepository.findByName(newIngredient.getName());
        logger.info("Finding out whether there's an existing ingredient with the same name.");
		
        if (existingIngredient != null) {
	        logger.error("There is an ingredient with the same name.");
			return new ResponseEntity<RESTError>(new RESTError(2, "Ingredient with that name already exists"), HttpStatus.CONFLICT);
		}
        
		Ingredients ingredient = new Ingredients();

		ingredient.setName(newIngredient.getName());
		ingredient.setUnit(newIngredient.getUnit());
		ingredient.setCalories(newIngredient.getCalories());
		ingredient.setCarbs(newIngredient.getCarbs());
		ingredient.setFats(newIngredient.getFats());
		ingredient.setSugars(newIngredient.getSugars());
		ingredient.setProteins(newIngredient.getProteins());
		ingredient.setSaturatedFats(newIngredient.getSaturatedFats());
		logger.info("Setting ingredient parameters.");
		
		// TODO resiti kako da dodajemo sastojcima alergene, kad cemo imati bazu sa
		// sastojcima bez alergena (da ne bude rucno)
		// ingredient.setAllergen(newIngredient.getAllergen());

		ingredientsRepository.save(ingredient);
		logger.info("Saving ingredient to the database");
		
		return new ResponseEntity<Ingredients>(ingredient, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "ingredient_id/{ingredient_id}/allergen_id/{allergen_id}")
	public ResponseEntity<?> addAllergenToIngredient(@PathVariable Integer ingredient_id,
			@PathVariable Integer allergen_id) {
		
		Ingredients ingredient = ingredientsRepository.findById(ingredient_id).get();
		
		if (ingredient == null) {
	        logger.error("There isn't an ingredient with id " + ingredient_id + "  in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Ingredient with that id is not in the database."), HttpStatus.NOT_FOUND);
		}
		
		Allergens allergen = allergensRepository.findById(allergen_id).get();
		
		if (allergen == null) {
	        logger.error("There isn't an allergen with id " + ingredient_id +" in the database.");
			return new ResponseEntity<RESTError>(new RESTError(2, "Allergen with that id is not in the database."), HttpStatus.NOT_FOUND);
		}

		ingredient.setAllergen(allergen);
		logger.info("Setting allergen to ingredient");
		ingredientsRepository.save(ingredient);
		logger.info("Saving ingredient");

		return new ResponseEntity<Ingredients>(ingredient, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "ingredient_id/{ingredient_id}")
	public ResponseEntity<?> deleteAllergenFromIngredient(@PathVariable Integer ingredient_id) {
		
		Ingredients ingredient = ingredientsRepository.findById(ingredient_id).get();
		
		if (ingredient == null) {
	        logger.error("There isn't an ingredient with that id" + ingredient_id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Ingredient with that id is not in the database."), HttpStatus.NOT_FOUND);
		}
		
		ingredient.setAllergen(null);
		logger.info("Deleting allergen from ingredient");
		ingredientsRepository.save(ingredient);
		logger.info("Saving ingredient");

		return new ResponseEntity<Ingredients>(ingredient, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateIngredient(@PathVariable Integer id, @RequestBody IngredientsDTO updatedIngredient) {
		Ingredients ingredient = ingredientsRepository.findById(id).get();

		if (ingredient == null) {
	        logger.error("There isn't an ingredient with id" + id + " in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No ingredient found with ID " + id), HttpStatus.NOT_FOUND);
		}

		ingredient.setName(updatedIngredient.getName());
		ingredient.setUnit(updatedIngredient.getUnit());
		ingredient.setCalories(updatedIngredient.getCalories());
		ingredient.setCarbs(updatedIngredient.getCarbs());
		ingredient.setFats(updatedIngredient.getFats());
		ingredient.setSugars(updatedIngredient.getSugars());
		ingredient.setProteins(updatedIngredient.getProteins());
		ingredient.setSaturatedFats(updatedIngredient.getSaturatedFats());
		ingredient.setAllergen(updatedIngredient.getAllergen());
		logger.info("Updating ingredient parameters.");

		ingredientsRepository.save(ingredient);
		logger.info("Saving ingredient");
		
		return new ResponseEntity<Ingredients>(ingredient, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{ingredient_id}")
	public ResponseEntity<?> deleteIngredient(@PathVariable Integer ingredient_id) {
		Optional<Ingredients> ingredient = ingredientsRepository.findById(ingredient_id);

		if (ingredient == null) {
			return new ResponseEntity<RESTError>(new RESTError(1, "No ingredient found with ID " + ingredient_id), HttpStatus.NOT_FOUND);
		}

		ingredientsRepository.delete(ingredient.get());
		return new ResponseEntity<>("Ingredient '" + ingredient.get().name + "' has been deleted successfully.", HttpStatus.OK);
	}

}
