package com.praksa.team4.services;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.EmailDTO;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.entities.dto.RecipeIdAmountDTO;
import com.praksa.team4.repositories.ChefRepository;
import com.praksa.team4.repositories.IngredientsRepository;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IngredientsRepository ingredientsRepository;

	@Autowired
	private ChefRepository chefRepository;

	@Autowired
	UserCustomValidator userValidator;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

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

	public ResponseEntity<?> getAllRecipesByChef(Authentication authentication) {
		
		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		if (currentUser.getRole().equals("ROLE_CHEF")) {
			Chef chef = (Chef) currentUser;
	
			List<Recipe> recipes = (List<Recipe>) recipeRepository.findByChef(chef);
	
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
		return new ResponseEntity<RESTError>(new RESTError(2, "User is not authorized to create recipes."), HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> createRecipe(RecipeIdAmountDTO newRecipe, BindingResult result, Authentication authentication) {

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
			recipe.setPicture(newRecipe.getPicture());
			recipe.setIsActive(true);
			recipe.setChef(chef);
			
			List<Ingredients> listIngredients = new ArrayList<>();
			
			for (Map.Entry<Integer, Integer> ingredientIdAmounts : newRecipe.getIngredientIdAmounts().entrySet()) {
				Integer id = ingredientIdAmounts.getKey();
				Integer amount = ingredientIdAmounts.getValue();
				Optional<Ingredients> newIngredient = ingredientsRepository.findById(id);
				if (newIngredient.isPresent() && newIngredient.get().getIsActive()) {
					newIngredient.get().setAmount(amount);
					listIngredients.add(newIngredient.get());
					logger.info("Adding ingredient to the database");
				}
			}

			recipe.setIngredients(listIngredients);
			
			recipe.setCalories(calculateCalories(recipe));

			recipe.setAmount(calculateRecipeAmount(recipe));
			
			recipeRepository.save(recipe);
			logger.info("Saving recipe to the database");
			
			emailServiceImpl.messageToAdmin(chef, recipe);
			
			return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe), HttpStatus.CREATED);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "User is not authorized to create recipes."),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> updateRecipe(RecipeIdAmountDTO updatedRecipe, BindingResult result, Integer id,
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
			recipe.get().setPicture(updatedRecipe.getPicture());
			
			List<Ingredients> listIngredients = new ArrayList<>();
			
			for (Map.Entry<Integer, Integer> ingredientIdAmounts : updatedRecipe.getIngredientIdAmounts().entrySet()) {
				Integer ingredientId = ingredientIdAmounts.getKey();
				Integer amount = ingredientIdAmounts.getValue();
				Optional<Ingredients> newIngredients = ingredientsRepository.findById(ingredientId);

				if (newIngredients.isPresent() && newIngredients.get().getIsActive()) {
					newIngredients.get().setAmount(amount);
					listIngredients.add(newIngredients.get());
				}
			}

			recipe.get().setIngredients(listIngredients);
			
			recipe.get().setCalories(calculateCalories(recipe.get()));

			recipe.get().setAmount(calculateRecipeAmount(recipe.get()));

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
				
				for (Map.Entry<Integer, Integer> ingredientIdAmounts : updatedRecipe.getIngredientIdAmounts().entrySet()) {
					Integer ingredientId = ingredientIdAmounts.getKey();
					Integer amount = ingredientIdAmounts.getValue();
					Optional<Ingredients> newIngredients = ingredientsRepository.findById(ingredientId);

					if (newIngredients.isPresent() && newIngredients.get().getIsActive()) {
						newIngredients.get().setAmount(amount);
						listIngredients.add(newIngredients.get());
					}
				}

				recipe.get().setIngredients(listIngredients);
				
				recipe.get().setCalories(calculateCalories(recipe.get()));

				recipeRepository.save(recipe.get());
				logger.info("Saving recipe to the database");
			}

			return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe.get()), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(3, "Not authorized to update recipe"),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> deleteRecipe(Integer id, Authentication authentication) {

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

	public ResponseEntity<?> deleteRecipeFromCookBook(@PathVariable Integer recipe_id, Authentication authentication) {

		Optional<Recipe> recipe = recipeRepository.findById(recipe_id);

		if (recipe.isEmpty() || !recipe.get().getIsActive()) {
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

			return new ResponseEntity<>("Recipe with ID " + recipe_id + " has been successfully deleted.",
					HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update recipe"),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> getAllRecipeByName(@RequestParam String name) {
	        ArrayList<Recipe> recipes = recipeRepository.findAllByName(name);

	        if (recipes.isEmpty()) {
	            return new ResponseEntity<RESTError>(new RESTError(1, "Recipes not found with name: " + name + "."),
	                    HttpStatus.NOT_FOUND);
	        } else {   
	        	ArrayList<RecipeDTO> activeRecipes = new ArrayList<>(); 
	        	for (Recipe recipe : recipes) {
	        		if (recipe.getIsActive()) {
	        			activeRecipes.add(new RecipeDTO(recipe));
	        			} 
	        		}
	        	return new ResponseEntity<ArrayList<RecipeDTO>>(activeRecipes, HttpStatus.OK);
	        }
	        
	    }

	public ResponseEntity<?> getRecipeById(@PathVariable Integer id) {
		Optional<Recipe> recipe = recipeRepository.findById(id);

		if (!recipe.isPresent() || !recipe.get().getIsActive()) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Recipe not found in the database."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RecipeDTO>(new RecipeDTO(recipe.get()), HttpStatus.OK);
	}

	public Float calculateCalories(Recipe recipe) {
		
		Float recipeCalories = 0.0f;
		
		for (Ingredients ingredients : recipe.getIngredients()) {
	        Float ingredientCalories = (ingredients.getCalories() / 100) * ingredients.getAmount();
	        recipeCalories += ingredientCalories;
		}
		return recipeCalories;
	}
	
	public Integer calculateRecipeAmount(Recipe recipe) {
		
		Integer recipeAmount = 0;
		
		for (Ingredients ingredients : recipe.getIngredients()) {
			recipeAmount += ingredients.getAmount();
		}
		return recipeAmount;
	}
}
