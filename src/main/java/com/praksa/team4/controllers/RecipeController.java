package com.praksa.team4.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.IngredientsDTO;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.ChefRepository;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.services.RecipeServiceImpl;
import com.praksa.team4.util.RESTError;

@RestController
@RequestMapping(path = "project/recipe")
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RecipeServiceImpl recipeServiceImpl;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllRecipes() {
		try {
			List<Recipe> recipes = (List<Recipe>) recipeRepository.findAll();

			if (recipes.isEmpty()) {
				logger.error("No recipes found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No recipes found"), HttpStatus.NOT_FOUND);
			} else {
				ArrayList<RecipeDTO> activeRecipes = new ArrayList<>();
				for (Recipe recipeDB : recipes) {
					if (recipeDB.getIsActive()) {
						activeRecipes.add(new RecipeDTO(recipeDB));
					}
				}

				logger.info("Found recipes in the database");
				return new ResponseEntity<ArrayList<RecipeDTO>>(activeRecipes, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/chefRecipes/{id}")
	public ResponseEntity<?> getAllRecipesByChef(@PathVariable Integer id) {
		Optional<Chef> chef = chefRepository.findById(id);
		
		if(chef.isEmpty() || !chef.get().getIsActive()) {
			logger.info("No chef found in the database");
			return new ResponseEntity<RESTError>(new RESTError(1, "No chef found in the database"), HttpStatus.OK);
		}

		List<Recipe> recipes = (List<Recipe>) recipeRepository.findByChef(chef.get());

		if (recipes.isEmpty()) {
			logger.error("No recipes found in the database.");
			return new ResponseEntity<RESTError>(new RESTError(2, "No recipes found in the database"), HttpStatus.NOT_FOUND);
		} else {
			ArrayList<RecipeDTO> activeRecipes = new ArrayList<>();
			for (Recipe recipeDB : recipes) {
				if (recipeDB.getIsActive()) {
					activeRecipes.add(new RecipeDTO(recipeDB));
				}
			}
			logger.info("Found recipes in the database");

			return new ResponseEntity<ArrayList<RecipeDTO>>(activeRecipes, HttpStatus.OK);

		}
	}

	@Secured({ "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.POST, value = "/newRecipe")
	public ResponseEntity<?> createRecipe(@Valid @RequestBody RecipeDTO newRecipe, BindingResult result,
			Authentication authentication) {
		return recipeServiceImpl.createRecipe(newRecipe, result, authentication);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.PUT, value = "/updateRecipe/{id}")
	public ResponseEntity<?> updateRecipe(@Valid @RequestBody RecipeDTO updatedRecipe, BindingResult result,
			@PathVariable Integer id,  Authentication authentication) {
		return recipeServiceImpl.updateRecipe(updatedRecipe, result, id, authentication);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_CHEF" })
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRecipe(@PathVariable Integer id, BindingResult result,  Authentication authentication) {
		return recipeServiceImpl.deleteRecipe(id, result, authentication);

	}

	@Secured({ "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.PUT, path = "recipe_id/{recipe_id}")
	public ResponseEntity<?> deleteRecipeFromCookBook(@PathVariable Integer recipe_id, Authentication authentication) {
		
		Optional<Recipe> recipe = recipeRepository.findById(recipe_id);

		if(recipe.isEmpty() || !recipe.get().getIsActive()) {
			logger.info("No recipe found in the database");
			return new ResponseEntity<RESTError>(new RESTError(1, "No recipe found in the database"), HttpStatus.OK);
		}
		
		String signedInUserEmail = authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(signedInUserEmail);

		if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			RegularUser user = (RegularUser) currentUser;
			if (user.getMyCookBook().getRecipes().contains(recipe.get()) && user.getMyCookBook().getIsActive()) {
				recipe.get().setMyCookBook(null); // proveriti sa i bez
				user.getMyCookBook().getRecipes().remove(recipe.get());
			}
			recipeRepository.save(recipe.get());

			return new ResponseEntity<>("Recipe with ID " + recipe_id + " has been successfully deleted.", HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update recipe"), HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRecipeByName(@RequestParam String name) {
		Optional<Recipe> recipe = recipeRepository.findByName(name);

		if (recipe.isEmpty() || !recipe.get().getIsActive()) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Recipe not found with name: " + name + "."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe.get()), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRecipeById(@PathVariable Integer id) {
		Optional<Recipe> recipe = recipeRepository.findById(id);

		if (!recipe.isPresent() || !recipe.get().getIsActive()) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Recipe not found in the database."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe.get()), HttpStatus.OK);
	}

}
