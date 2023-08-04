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
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.ChefRepository;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	private IngredientsRepository ingredientsRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());


	public ResponseEntity<?> createRecipe(RecipeDTO newRecipe, BindingResult result,
			Authentication authentication) {

		if (result.hasErrors()) {
			logger.error("Sent incorrect parameters.");
			return new ResponseEntity<>(ErrorMessageHelper.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		String signedInUserEmail = authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(signedInUserEmail);

		Recipe existingRecipe = recipeRepository.findByName(newRecipe.getName());
		logger.info("Checking whether theres an existing recipe in the database");

		if (existingRecipe != null) {
			logger.error("Recipe with the same name already exists");
			return new ResponseEntity<RESTError>(new RESTError(1, "A recipe with the same name already exists"),
					HttpStatus.CONFLICT);
		}
		Recipe recipe = new Recipe();

		recipe.setName(newRecipe.getName());
		recipe.setTime(newRecipe.getTime());
		recipe.setSteps(newRecipe.getSteps());
		recipe.setAmount(newRecipe.getAmount());
		recipe.setPicture(newRecipe.getPicture());
		// TODO for chef get TOKEN
		List<Ingredients> listIngredients = new ArrayList<>();
		for (Ingredients ingredients : newRecipe.getIngredients()) {
			Ingredients newIngredients = ingredientsRepository.findById(ingredients.getId()).get();
			listIngredients.add(newIngredients);
		}
		recipe.setIngredients(listIngredients);

		recipeRepository.save(recipe);
		logger.info("Saving recipe to the database");
		if (result.hasErrors()) {
			logger.info("Validating input parameters for recipe");
			return new ResponseEntity<>(ErrorMessageHelper.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Recipe>(recipe, HttpStatus.CREATED);
	}

	public ResponseEntity<?> updateRecipe(RecipeDTO updatedRecipe, BindingResult result, Integer id) {

		if (result.hasErrors()) {
			logger.info("Validating input parameters for recipe");
			return new ResponseEntity<>(ErrorMessageHelper.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		Recipe recipe = recipeRepository.findById(id).orElse(null);

		if (recipe == null) {
			logger.error("No recipe with " + id + " ID found");
			return new ResponseEntity<RESTError>(new RESTError(1, "No recipe with " + id + " ID found"),
					HttpStatus.NOT_FOUND);
		}

		recipe.setName(updatedRecipe.getName());
		recipe.setTime(updatedRecipe.getTime());
		recipe.setSteps(updatedRecipe.getSteps());
		recipe.setAmount(updatedRecipe.getAmount());

		recipeRepository.save(recipe);
		logger.info("Saving recipe to the database");

		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}

	public ResponseEntity<?> deleteRecipe(Integer id, BindingResult result) {

		Optional<Recipe> recipe = recipeRepository.findById(id);

		if (recipe.isPresent()) {
			for (Ingredients ingredients : recipe.get().getIngredients()) {
				recipe.get().getIngredients().remove(ingredients);
				ingredientsRepository.save(ingredients);
			}
			recipeRepository.delete(recipe.get());
			logger.info("Deleting recipe from the database");
			return new ResponseEntity<>("Recipe with ID " + id + " has been successfully deleted.", HttpStatus.OK);
		} else {
			logger.error("There is no recipe found with " + id);
			return new ResponseEntity<RESTError>(new RESTError(1, "No recipe found"), HttpStatus.NOT_FOUND);
		}
	}
}
