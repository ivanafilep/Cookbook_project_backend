package com.praksa.team4.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RecipeIngredient;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.repositories.RecipeIngredientRepository;
import com.praksa.team4.repositories.RecipeRepository;

public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {
	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private RecipeIngredientRepository recipeIngredientRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> addIngredientsToRecipe(@PathVariable Integer ingredientId,
			@PathVariable Integer recipeId) {

		Optional<Ingredients> ingredient = ingredientsRepository.findById(ingredientId);
		Optional<Recipe> recipe = recipeRepository.findById(recipeId);

		if (!ingredient.isPresent() || !recipe.isPresent()) {
			return new ResponseEntity<>("Ingredient or recipe are not found.", HttpStatus.NOT_FOUND);
		}

		RecipeIngredient recipeIngredients = new RecipeIngredient();
		recipeIngredients.setIngredients(ingredient.get());
		recipeIngredients.setRecipe(recipe.get());
		recipeIngredientRepository.save(recipeIngredients);

		logger.info("", ingredientId, recipeId);
		return new ResponseEntity<>("Ingredients are succesfully added to recipe", HttpStatus.OK);
	}
}
