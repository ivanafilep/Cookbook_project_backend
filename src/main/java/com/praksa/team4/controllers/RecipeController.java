package com.praksa.team4.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.services.RecipeServiceImpl;

@RestController
@RequestMapping(path = "project/recipe")
public class RecipeController {

	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	private RecipeServiceImpl recipeServiceImpl;

	// pregled liste svih recepata
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(method = RequestMethod.GET)
	private ResponseEntity<?> getAllRecipes() {
		Iterable<Recipe> recipes = (Iterable<Recipe>) recipeRepository.findAll();
		return new ResponseEntity<>(recipes, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(method = RequestMethod.POST, value = "/newRecipe")
	public ResponseEntity<?> createRecipe(@Valid @RequestBody RecipeDTO newRecipe, BindingResult result) {
		return recipeServiceImpl.createRecipe(newRecipe, result);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(method = RequestMethod.PUT, value = "/updateRecipe/{id}")
	public ResponseEntity<?> updateRecipe(@Valid @RequestBody RecipeDTO updatedRecipe, BindingResult result,
			@PathVariable Integer id) {
		return recipeServiceImpl.updateRecipe(updatedRecipe, result, id);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRecipe(@PathVariable Integer id) {
		Optional<Recipe> recipe = recipeRepository.findById(id);
		if (recipe == null) {
			return new ResponseEntity<>("No ingredient found with ID " + id, HttpStatus.NOT_FOUND);
		}

		for (Ingredients ingredient : recipe.get().getIngredients()) {
			recipe.get().getIngredients().remove(ingredient);
			ingredientsRepository.save(ingredient);
		}
		// TODO delete from MyCookBook

		recipeRepository.delete(recipe.get());
		return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRecipeByName(@RequestParam String name) {
		Recipe recipe = recipeRepository.findByName(name);

		if (recipe == null) {
			return new ResponseEntity<>("Recipe not found!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(recipe, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRecipeById(@PathVariable Integer id) {
		Optional<Recipe> recipe = recipeRepository.findById(id);

		if (!recipe.isPresent()) {
			return new ResponseEntity<>("Recipe not found!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(recipe.get(), HttpStatus.OK);
	}

}
