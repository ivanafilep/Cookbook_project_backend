package com.praksa.team4.controllers;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.services.RecipeServiceImpl;

@RestController
@RequestMapping(path = "project/recipe")
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {

	@Autowired
	private RecipeServiceImpl recipeService;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllRecipes() {
		return recipeService.getAllRecipes();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/chefRecipes/{id}")
	public ResponseEntity<?> getAllRecipesByChef(@PathVariable Integer id) {
		return recipeService.getAllRecipesByChef(id);
	}

	@Secured({ "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.POST, value = "/newRecipe")
	public ResponseEntity<?> createRecipe(@Valid @RequestBody RecipeDTO newRecipe, BindingResult result,
			Authentication authentication) {
		return recipeService.createRecipe(newRecipe, result, authentication);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.PUT, value = "/updateRecipe/{id}")
	public ResponseEntity<?> updateRecipe(@Valid @RequestBody RecipeDTO updatedRecipe, BindingResult result,
			@PathVariable Integer id, Authentication authentication) {
		return recipeService.updateRecipe(updatedRecipe, result, id, authentication);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRecipe(@PathVariable Integer id, BindingResult result,
			Authentication authentication) {
		return recipeService.deleteRecipe(id, result, authentication);

	}

	@Secured({ "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "recipe_id/{recipe_id}")
	public ResponseEntity<?> deleteRecipeFromCookBook(@PathVariable Integer recipe_id, Authentication authentication) {
		return recipeService.deleteRecipeFromCookBook(recipe_id, authentication);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRecipeByName(@RequestParam String name) {
		return recipeService.getAllRecipeByName(name);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRecipeById(@PathVariable Integer id) {
		return recipeService.getRecipeById(id);
	}

}
