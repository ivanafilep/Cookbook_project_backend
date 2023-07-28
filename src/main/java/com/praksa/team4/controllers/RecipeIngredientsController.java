package com.praksa.team4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.services.RecipeIngredientsServiceImpl;

@RestController
@RequestMapping(path = "project/recipeIngredients")
public class RecipeIngredientsController {
	
	@Autowired
	private RecipeIngredientsServiceImpl recipeIngredientsServiceImpl;

	// TODO GET

	@RequestMapping(method = RequestMethod.POST, path = "/ingredients/{ingredientId}/recipe/{recipeId}")
	public ResponseEntity<?> addIngredientsToRecipe(@PathVariable Integer ingredientId,
			@PathVariable Integer recipeId) {
		return recipeIngredientsServiceImpl.addIngredientsToRecipe(ingredientId, recipeId);
	}
	// TODO PUT

	// TODO DELETE
}
