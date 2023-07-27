package com.praksa.team4.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.services.RecipeServiceImpl;

@RestController
@RequestMapping(path = "project/recipe")
public class RecipeController {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private RecipeServiceImpl recipeServiceImpl;

	// pregled liste svih recepata
	@RequestMapping(method = RequestMethod.GET)
	private ResponseEntity<?> getAllRecipes() {
		Iterable<Recipe> recipes = (Iterable<Recipe>) recipeRepository.findAll();
		return new ResponseEntity<>(recipes, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/newRecipe")
	public ResponseEntity<?> createRecipe(@Valid @RequestBody RecipeDTO newRecipe, BindingResult result) {
		return recipeServiceImpl.createRecipe(newRecipe, result);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/updateRecipe/{id}")
	public ResponseEntity<?> updateRecipe(@Valid @RequestBody RecipeDTO updatedRecipe, BindingResult result,
			@PathVariable Integer id) {
		return recipeServiceImpl.updateRecipe(updatedRecipe, result, id);
	}
	
	@RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
	private String getRecipeByName(@PathVariable("name") String Name) {
		return null;
	}
}
