package com.praksa.team4.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private IngredientsRepository ingredientsRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> createRecipe(RecipeDTO newRecipe, BindingResult result) {

		if (result.hasErrors()) {
			logger.info("Validating input parameters for recipe");
			return new ResponseEntity<>(ErrorMessageHelper.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		// TODO if for chef
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
		// TODO get Ingredients (id)

//		Ingredients ingredient = ingredientsRepository.findByName(newRecipe.getIngredients());
//		if(recipeIngredients.isEmpty()){
//			//RecipeIngredient ri = new RecipeIngredient();
//			Ingredients ingredient = new Ingredients();
//			ingredient.setRecipeIngredient(newRecipe.getRecipeIngredients());
//			genreRepository.save(g);
//			b.setGenre(g);
//		}else {
//			b.setGenre(lg.get(0));
//		}

		recipeRepository.save(recipe);
		logger.info("Saving recipe to the database");

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

	public ResponseEntity<?> deleteRecipe(RecipeDTO deletedRecipe, BindingResult result) {
		return null;
	}
}
