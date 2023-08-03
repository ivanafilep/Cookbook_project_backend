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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.dto.AllergensDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;

@RestController
@RequestMapping(path = "project/allergens")
public class AllergensController {
	
	@Autowired
	private AllergensRepository allergensRepository;

	@Autowired
	private IngredientsRepository ingredientsRepository;
	
	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			List<Allergens> allergens = (List<Allergens>) allergensRepository.findAll();

			if (allergens.isEmpty()) {
		        logger.error("No allergens found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No allergens found"), HttpStatus.NOT_FOUND);
			} else {
		        logger.info("Found allergens in the database");
				return new ResponseEntity<Iterable<Allergens>>(allergensRepository.findAll(), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAllergen(@Valid @RequestBody AllergensDTO allergen, BindingResult result) {
		
		if (result.hasErrors()) {
	        logger.error("Sent incorrect parameters.");
			return new ResponseEntity<RESTError>(new RESTError(1,ErrorMessageHelper.createErrorMessage(result)), HttpStatus.BAD_REQUEST);
		}
		
		Allergens newAllergen = new Allergens();

		newAllergen.setName(allergen.getName());
		newAllergen.setIcon(allergen.getIcon());
		logger.info("Setting allergen parameters.");

		allergensRepository.save(newAllergen);
		logger.info("Saving allergen to the database");

		return new ResponseEntity<Allergens>(newAllergen, HttpStatus.CREATED);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateAllergen(@PathVariable Integer id, @RequestBody AllergensDTO updatedAllergen) {
		Optional<Allergens> allergen = allergensRepository.findById(id);
		
		if (allergen.isEmpty()) {
	        logger.error("There isn't an allergen with id " + id + "  in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Allergen with that id is not in the database."), HttpStatus.NOT_FOUND);
		}

		allergen.get().setName(updatedAllergen.getName());
		allergen.get().setIcon(updatedAllergen.getIcon());

		allergensRepository.save(allergen.get());
		return new ResponseEntity<Allergens>(allergen.get(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteAllergen(@PathVariable Integer id) {
		Optional<Allergens> allergen = allergensRepository.findById(id);
		
		if (allergen.isEmpty()) {
			logger.error("No allergen with " + id + " found.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No allergen with " + id + " found."), HttpStatus.NOT_FOUND);
		} else {

			if (!allergen.get().getIngredient().isEmpty()) {
				for (Ingredients ingredient : allergen.get().getIngredient()) {
					ingredient.setAllergen(null);
					logger.info("For each ingredient set allergen null, while deleting that allergen.");
					ingredientsRepository.save(ingredient);
					logger.info("Saving ingredient.");
				}
			}

			allergensRepository.delete(allergen.get());
			logger.info("Deleting allergen from database.");
			
			return new ResponseEntity<RESTError>(new RESTError(2, "Allergen has been successfully deleted"), HttpStatus.OK);
		}

	}
}
