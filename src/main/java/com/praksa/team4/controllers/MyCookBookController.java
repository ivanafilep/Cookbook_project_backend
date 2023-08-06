package com.praksa.team4.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.Admin;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.IngredientsDTO;
import com.praksa.team4.entities.dto.MyCookBookDTO;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.CookBookRepository;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.repositories.RegularUserRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.RESTError;

@RestController
@RequestMapping(path = "project/cookbook")
@CrossOrigin(origins = "http://localhost:3000")
public class MyCookBookController {

	@Autowired
	private CookBookRepository cookBookRepository;

	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private UserRepository userRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		try {
			List<MyCookBook> myCookBook = (List<MyCookBook>) cookBookRepository.findAll();

			if (myCookBook.isEmpty()) {
		        logger.error("No cookbook found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No cookbook found"), HttpStatus.NOT_FOUND);
			} else {
				ArrayList<MyCookBookDTO> activeCookbook = new ArrayList<>();
				for(MyCookBook cookbookDB : myCookBook) {
					if (cookbookDB.getIsActive()) {
						activeCookbook.add(new MyCookBookDTO(cookbookDB));
					}
				}
		        logger.info("Found cookbook in the database");
				return new ResponseEntity<ArrayList<MyCookBookDTO>>(activeCookbook, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError(2, "Exception occurred: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_REGULAR_USER"})
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getCookBookById(@PathVariable Integer id, Authentication authentication) {

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		Optional<MyCookBook> myCookBook = cookBookRepository.findById(id);
		
		if (!myCookBook.isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError(1, "CookBook is not found!"), HttpStatus.NOT_FOUND);
		}
		
		if (currentUser.getRole().equals("ROLE_ADMIN")) {
			ArrayList<RecipeDTO> cookbookRecipes = new ArrayList<>();
			for (Recipe recipe : myCookBook.get().getRecipes()) {
				if(recipe.getIsActive()) {
					cookbookRecipes.add(new RecipeDTO(recipe));
				}
			}
			return new ResponseEntity<ArrayList<RecipeDTO>>(new RecipeDTO(cookbookRecipes), HttpStatus.OK);
		} else if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname() + " is looking at his own cookbook.");
			RegularUser regularUser = (RegularUser) currentUser;

			if (regularUser.getMyCookBook().getId().equals(myCookBook.get().getId())) {
				logger.info("Regular user is updating his own cookbook.");
				ArrayList<RecipeDTO> cookbookRecipes = new ArrayList<>();
				for (Recipe recipe : myCookBook.get().getRecipes()) {
					if(recipe.getIsActive()) {
						cookbookRecipes.add(new RecipeDTO(recipe));
					}
				}
			}
			return new ResponseEntity<ArrayList<RecipeDTO>>(new RecipeDTO(cookbookRecipes), HttpStatus.OK);
		}
		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update regular user"), HttpStatus.UNAUTHORIZED);
	}

	@Secured("ROLE_REGULAR_USER")
	@RequestMapping(method = RequestMethod.PUT, path = "cookbook_id/{cookbook_id}/recipe_id/{recipe_id}")
	public ResponseEntity<?> addNewRecipeToMyCookBook(@PathVariable Integer cookbook_id,
			@PathVariable Integer recipe_id, Authentication authentication) {

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		Optional<MyCookBook> myCookBook = cookBookRepository.findById(cookbook_id);
		
		if (!myCookBook.isPresent()) {
			logger.info("No cookbook found in the database");
			return new ResponseEntity<RESTError>(new RESTError(1, "No cookbook found in the database with "+ cookbook_id + " ID."), HttpStatus.NOT_FOUND);
		}
		
		Optional<Recipe> recipe = recipeRepository.findById(recipe_id);

		if (!recipe.isPresent()) {
			logger.info("Found recipe in the database");
			return new ResponseEntity<RESTError>(new RESTError(2, "No recipe found in the database with "+ recipe_id + " ID."), HttpStatus.NOT_FOUND);
		}
		
		if (myCookBook.get().getRecipes().contains(recipe.get())) {
			logger.info("Already have this recipe in my cookook");
			return new ResponseEntity<RESTError>(new RESTError(3, "There's already this recipe in my cookbook"), HttpStatus.CONFLICT);
		}
		
		if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname() + " is adding recipe to his own cookbook.");
			RegularUser regularUser = (RegularUser) currentUser;

			if (regularUser.getMyCookBook().getId().equals(myCookBook.get().getId())) {
				logger.info("Regular user is updating his own cookbook.");
				myCookBook.get().getRecipes().add(recipe.get());
//				recipe.get().getMyCookBook().add(myCookBook.get()); // TODO treba proveriti bez ove
			}
		}
		
//		recipeRepository.save(recipe.get());// TODO treba proveriti bez ove
		cookBookRepository.save(myCookBook.get());

		return new ResponseEntity<MyCookBookDTO>(new MyCookBookDTO(myCookBook.get()), HttpStatus.CREATED);
	}

	@Secured("ROLE_REGULAR_USER")
	@RequestMapping(method = RequestMethod.PUT, path = "update/cookbook_id/{cookbook_id}/recipe_id/{recipe_id}")
	public ResponseEntity<?> updateMyCookBook(@PathVariable Integer cookbook_id, @PathVariable Integer recipe_id, Authentication authentication) {
		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);
		
		Optional<MyCookBook> myCookBook = cookBookRepository.findById(cookbook_id);
		
		if (!myCookBook.isPresent()) {
			logger.info("No cookbook found in the database");
			return new ResponseEntity<RESTError>(new RESTError(1, "No cookbook found in the database with "+ cookbook_id + " ID."), HttpStatus.NOT_FOUND);
		}
		
		Optional<Recipe> recipe = recipeRepository.findById(recipe_id);

		if (!recipe.isPresent()) {
			logger.info("Found recipe in the database");
			return new ResponseEntity<RESTError>(new RESTError(2, "No recipe found in the database with "+ recipe_id + " ID."), HttpStatus.NOT_FOUND);
		}
		
		if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname() + " is updating his own cookbook.");
			RegularUser regularUser = (RegularUser) currentUser;

			if (regularUser.getMyCookBook().getId().equals(myCookBook.get().getId())) {
				logger.info("Regular user is updating his own cookbook.");
				myCookBook.get().getRecipes().remove(recipe.get());
				cookBookRepository.save(myCookBook.get());
			}
		}

		return new ResponseEntity<>(myCookBook, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteMyCookBook(@PathVariable Integer id) {
		Optional<MyCookBook> myCookBook = cookBookRepository.findById(id);
		Optional<RegularUser> regularUser = recipeRepository.findByMyCookBook(myCookBook.get());
		
		if (myCookBook.isEmpty()) {
			return new ResponseEntity<RESTError>(new RESTError(1, "There is no cookbook with " + id + " found in the database" ), HttpStatus.NOT_FOUND);
		}else if (regularUser.isEmpty()) {
			return new ResponseEntity<RESTError>(new RESTError(2, "There is no regular user with " + regularUser.get().getId() + " found in the database" ), HttpStatus.NOT_FOUND);
		} else {
			myCookBook.get().setRegularUser(null);
			cookBookRepository.delete(myCookBook.get());
			return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
		}
	}
}
