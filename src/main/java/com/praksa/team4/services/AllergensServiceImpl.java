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
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.dto.AllergensDTO;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@Service
public class AllergensServiceImpl implements AllergensService {

	@Autowired
	private AllergensRepository allergensRepository;

	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	UserCustomValidator userValidator;
	
	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> getAll() {
		try {
			List<Allergens> allergens = (List<Allergens>) allergensRepository.findAll();

			if (allergens.isEmpty()) {
				logger.error("No allergens found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No allergens found"), HttpStatus.NOT_FOUND);
			} else {
				ArrayList<AllergensDTO> activeAllergens = new ArrayList<>();
				for (Allergens allergenDB : allergens) {
					if (allergenDB.getIsActive()) {
						activeAllergens.add(new AllergensDTO(allergenDB));
					}
				}

				logger.info("Found allergens in the database");
				return new ResponseEntity<ArrayList<AllergensDTO>>(activeAllergens, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getById(@PathVariable Integer id) {
		Optional<Allergens> allergen = allergensRepository.findById(id);

		if (allergen.isPresent() && allergen.get().getIsActive()) {
			logger.info("Found allergen in the database");
			return new ResponseEntity<AllergensDTO>(new AllergensDTO(allergen.get()), HttpStatus.OK);
		} else {
			logger.error("No allergen found in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No allergen found with ID " + id),
					HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<?> getByName(@PathVariable String name) {
		Optional<Allergens> allergen = allergensRepository.findByName(name);

		if (allergen.isPresent() && allergen.get().getIsActive()) {
			logger.info("Found allergen in the database");
			return new ResponseEntity<AllergensDTO>(new AllergensDTO(allergen.get()), HttpStatus.OK);
		} else {
			logger.error("No allergen found in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No allergen found with name: " + name),
					HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<?> addNewAllergen(@Valid @RequestBody AllergensDTO allergen, BindingResult result) {

		if (result.hasErrors()) {
			logger.error("Sent incorrect parameters.");
			return new ResponseEntity<RESTError>(new RESTError(1, ErrorMessageHelper.createErrorMessage(result)),
					HttpStatus.BAD_REQUEST);
		}

		Allergens newAllergen = new Allergens();

		newAllergen.setName(allergen.getName());
		newAllergen.setIcon(allergen.getIcon());
		newAllergen.setIsActive(true);
		logger.info("Setting allergen parameters.");

		allergensRepository.save(newAllergen);
		logger.info("Saving allergen to the database");

		return new ResponseEntity<AllergensDTO>(new AllergensDTO(newAllergen), HttpStatus.CREATED);

	}

	public ResponseEntity<?> updateAllergen(@PathVariable Integer id, @RequestBody AllergensDTO updatedAllergen) {
		Optional<Allergens> allergen = allergensRepository.findById(id);

		if (allergen.isEmpty() || !allergen.get().getIsActive()) {
			logger.error("There isn't an allergen with id " + id + "  in the database.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Allergen with that id is not in the database."),
					HttpStatus.NOT_FOUND);
		}

		allergen.get().setName(updatedAllergen.getName());
		allergen.get().setIcon(updatedAllergen.getIcon());

		allergensRepository.save(allergen.get());
		return new ResponseEntity<AllergensDTO>(new AllergensDTO(allergen.get()), HttpStatus.OK);
	}

	public ResponseEntity<?> deleteAllergen(@PathVariable Integer id) {
		Optional<Allergens> allergen = allergensRepository.findById(id);

		if (allergen.isEmpty() || !allergen.get().getIsActive()) {
			logger.error("No allergen with " + id + " found.");
			return new ResponseEntity<RESTError>(new RESTError(1, "No allergen with " + id + " found."),
					HttpStatus.NOT_FOUND);
		} else if (!allergen.get().getIngredient().isEmpty()) {
			for (Ingredients ingredient : allergen.get().getIngredient()) {
				if (ingredient.getIsActive()) {
					ingredient.setAllergen(null);
					logger.info("For each ingredient set allergen null, while deleting that allergen.");
					ingredientsRepository.save(ingredient);
					logger.info("Saving ingredient.");
				}
			}
			allergen.get().setIsActive(false);
			logger.info("Setting allergen to archive.");

			allergensRepository.save(allergen.get());
			logger.info("Deleting allergen from database.");
		}

		return new ResponseEntity<>("Allergen has been successfully deleted", HttpStatus.OK);
	}
}
