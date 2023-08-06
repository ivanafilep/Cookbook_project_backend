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

	public ResponseEntity<?> createRecipe(RecipeDTO newRecipe, BindingResult result, Authentication authentication) {

		if (result.hasErrors()) {
			logger.error("Sent incorrect parameters.");
			return new ResponseEntity<RESTError>(new RESTError(1, ErrorMessageHelper.createErrorMessage(result)),
					HttpStatus.BAD_REQUEST);
		}

		String signedInUserEmail = authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(signedInUserEmail);

		if (currentUser.getRole().equals("ROLE_CHEF")) {
			Chef chef = (Chef) currentUser;

			Recipe recipe = new Recipe();

			recipe.setName(newRecipe.getName());
			recipe.setTime(newRecipe.getTime());
			recipe.setSteps(newRecipe.getSteps());
			recipe.setAmount(newRecipe.getAmount());
			recipe.setPicture(newRecipe.getPicture());
			recipe.setChef(chef);
			List<Ingredients> listIngredients = new ArrayList<>();
			for (Ingredients ingredients : newRecipe.getIngredients()) {
				if (ingredients.getIsActive()) {
					Ingredients newIngredients = ingredientsRepository.findById(ingredients.getId()).get();
					listIngredients.add(newIngredients);
				}
			}
			recipe.setIngredients(listIngredients);

			recipeRepository.save(recipe);
			logger.info("Saving recipe to the database");

			return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe), HttpStatus.CREATED);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "User is not authorized to create recipes."),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> updateRecipe(RecipeDTO updatedRecipe, BindingResult result, Integer id,
			Authentication authentication) {

		if (result.hasErrors()) {
			logger.info("Validating input parameters for recipe");
			return new ResponseEntity<RESTError>(new RESTError(1, ErrorMessageHelper.createErrorMessage(result)),
					HttpStatus.BAD_REQUEST);
		}

		Optional<Recipe> recipe = recipeRepository.findById(id);

		if (recipe.isEmpty() || !recipe.get().getIsActive()) {
			logger.error("No recipe with " + id + " ID found");
			return new ResponseEntity<RESTError>(new RESTError(2, "No recipe with " + id + " ID found"),
					HttpStatus.NOT_FOUND);
		}

		String signedInUserEmail = authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(signedInUserEmail);

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin is updating the recipe from the database");

			recipe.get().setName(updatedRecipe.getName());
			recipe.get().setTime(updatedRecipe.getTime());
			recipe.get().setSteps(updatedRecipe.getSteps());
			recipe.get().setAmount(updatedRecipe.getAmount());
			recipe.get().setPicture(updatedRecipe.getPicture());
			List<Ingredients> listIngredients = new ArrayList<>();
			for (Ingredients ingredients : updatedRecipe.getIngredients()) {
				if (ingredients.getIsActive()) {
					Ingredients newIngredients = ingredientsRepository.findById(ingredients.getId()).get();
					listIngredients.add(newIngredients);
				}
			}
			recipe.get().setIngredients(listIngredients);

			recipeRepository.save(recipe.get());
			logger.info("Saving recipe to the database");

			return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe.get()), HttpStatus.OK);
		} else if (currentUser.getRole().equals("ROLE_CHEF")) {
			Chef chef = (Chef) currentUser;
			if (chef.getRecipes().contains(recipe.get())) {
				recipe.get().setName(updatedRecipe.getName());
				recipe.get().setTime(updatedRecipe.getTime());
				recipe.get().setSteps(updatedRecipe.getSteps());
				recipe.get().setAmount(updatedRecipe.getAmount());
				recipe.get().setPicture(updatedRecipe.getPicture());
				List<Ingredients> listIngredients = new ArrayList<>();
				for (Ingredients ingredients : updatedRecipe.getIngredients()) {
					if (ingredients.getIsActive()) {
						Ingredients newIngredients = ingredientsRepository.findById(ingredients.getId()).get();
						listIngredients.add(newIngredients);
					}
				}
				recipe.get().setIngredients(listIngredients);

				recipeRepository.save(recipe.get());
				logger.info("Saving recipe to the database");
			}

			return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe.get()), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update recipe"),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> deleteRecipe(Integer id, BindingResult result, Authentication authentication) {

		Optional<Recipe> recipe = recipeRepository.findById(id);

		String signedInUserEmail = authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(signedInUserEmail);

		if (!recipe.isPresent()) {
			logger.error("There is no recipe found with " + id);
			return new ResponseEntity<RESTError>(new RESTError(1, "No recipe found"), HttpStatus.NOT_FOUND);
		}

		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			logger.info("Admin is deleting recipe from the database");
			for (Ingredients ingredients : recipe.get().getIngredients()) {
				recipe.get().getIngredients().remove(ingredients);
				ingredientsRepository.save(ingredients);
			}
			recipe.get().setIsActive(false);
			recipeRepository.save(recipe.get());
			logger.info("Deleting recipe from the database");
			return new ResponseEntity<>("Recipe with ID " + id + " has been successfully deleted.", HttpStatus.OK);

		} else if (currentUser.getRole().equals("ROLE_CHEF")) {
			Chef chef = (Chef) currentUser;
			if (chef.getRecipes().contains(recipe.get())) {
				for (Ingredients ingredients : recipe.get().getIngredients()) {
					recipe.get().getIngredients().remove(ingredients);
					ingredientsRepository.save(ingredients);
				}
				recipe.get().setIsActive(false);
				recipeRepository.save(recipe.get());
				logger.info("Deleting recipe from the database");
			}
			return new ResponseEntity<>("Recipe with ID " + id + " has been successfully deleted.", HttpStatus.OK);

		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update recipe"),
				HttpStatus.UNAUTHORIZED);
	}
}
