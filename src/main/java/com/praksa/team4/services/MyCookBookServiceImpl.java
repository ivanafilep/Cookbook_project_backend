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
import org.springframework.web.bind.annotation.PathVariable;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.MyCookBookDTO;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.MyCookBookRepository;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.repositories.UserRepository;
import com.praksa.team4.util.RESTError;
import com.praksa.team4.util.UserCustomValidator;

@Service
public class MyCookBookServiceImpl implements MyCookBookService {

	@Autowired
	private MyCookBookRepository cookBookRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	UserCustomValidator userValidator;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> getAll() {
		try {
			List<MyCookBook> myCookBook = (List<MyCookBook>) cookBookRepository.findAll();

			if (myCookBook.isEmpty()) {
				logger.error("No cookbook found in the database.");
				return new ResponseEntity<RESTError>(new RESTError(1, "No cookbook found"), HttpStatus.NOT_FOUND);
			} else {
				ArrayList<MyCookBookDTO> activeCookbook = new ArrayList<>();
				for (MyCookBook cookbookDB : myCookBook) {
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

	public ResponseEntity<?> getCookBookByUser(Authentication authentication) {

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname()
					+ " is looking at his own cookbook.");

			RegularUser regularUser = (RegularUser) currentUser;
			MyCookBook myCookBook = regularUser.getMyCookBook();
			
			logger.info("Regular user is updating his own cookbook.");
			ArrayList<RecipeDTO> cookbookRecipes = new ArrayList<>();
			if(!myCookBook.getRecipes().isEmpty()) {
				for (Recipe recipe : myCookBook.getRecipes()) {
					if (recipe.getIsActive()) {
						cookbookRecipes.add(new RecipeDTO(recipe));
					}
				}
				return new ResponseEntity<ArrayList<RecipeDTO>>(cookbookRecipes, HttpStatus.OK);

			} else {
				return new ResponseEntity<ArrayList<RecipeDTO>>(cookbookRecipes, HttpStatus.OK);
			}
			
		}
		
		return new ResponseEntity<RESTError>(new RESTError(2, "Not authorized to update cookbook"),
				HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> addNewRecipeToMyCookBook(@PathVariable Integer cookbook_id,
			@PathVariable Integer recipe_id, Authentication authentication) {

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		Optional<MyCookBook> myCookBook = cookBookRepository.findById(cookbook_id);

		if (!myCookBook.isPresent() || !myCookBook.get().getIsActive()) {
			logger.info("No cookbook found in the database");
			return new ResponseEntity<RESTError>(
					new RESTError(1, "No cookbook found in the database with " + cookbook_id + " ID."),
					HttpStatus.NOT_FOUND);
		}

		Optional<Recipe> recipe = recipeRepository.findById(recipe_id);

		if (!recipe.isPresent() || !recipe.get().getIsActive()) {
			logger.info("Found recipe in the database");
			return new ResponseEntity<RESTError>(
					new RESTError(2, "No recipe found in the database with " + recipe_id + " ID."),
					HttpStatus.NOT_FOUND);
		}

		if (myCookBook.get().getRecipes().contains(recipe.get())) {
			logger.info("Already have this recipe in my cookook");
			return new ResponseEntity<RESTError>(new RESTError(3, "There's already this recipe in my cookbook"),
					HttpStatus.CONFLICT);
		}

		if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname()
					+ " is adding recipe to his own cookbook.");
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

	public ResponseEntity<?> updateMyCookBook(@PathVariable Integer cookbook_id, @PathVariable Integer recipe_id,
			Authentication authentication) {

		String email = (String) authentication.getName();
		UserEntity currentUser = userRepository.findByEmail(email);

		Optional<MyCookBook> myCookBook = cookBookRepository.findById(cookbook_id);

		if (!myCookBook.isPresent() || !myCookBook.get().getIsActive()) {
			logger.info("No cookbook found in the database");
			return new ResponseEntity<RESTError>(
					new RESTError(1, "No cookbook found in the database with " + cookbook_id + " ID."),
					HttpStatus.NOT_FOUND);
		}

		Optional<Recipe> recipe = recipeRepository.findById(recipe_id);

		if (!recipe.isPresent() || !recipe.get().getIsActive()) {
			logger.info("Found recipe in the database");
			return new ResponseEntity<RESTError>(
					new RESTError(2, "No recipe found in the database with " + recipe_id + " ID."),
					HttpStatus.NOT_FOUND);
		}

		if (currentUser.getRole().equals("ROLE_REGULAR_USER")) {
			logger.info("Regular user" + currentUser.getName() + " " + currentUser.getLastname()
					+ " is updating his own cookbook.");
			RegularUser regularUser = (RegularUser) currentUser;

			if (regularUser.getMyCookBook().getId().equals(myCookBook.get().getId())) {
				logger.info("Regular user is updating his own cookbook.");
				myCookBook.get().getRecipes().remove(recipe.get());
				cookBookRepository.save(myCookBook.get());
			}
		}

		return new ResponseEntity<MyCookBookDTO>(new MyCookBookDTO(myCookBook.get()), HttpStatus.OK);
	}

	public ResponseEntity<?> deleteMyCookBook(@PathVariable Integer id) {
		Optional<MyCookBook> myCookBook = cookBookRepository.findById(id);
		Optional<RegularUser> regularUser = recipeRepository.findByMyCookBook(myCookBook.get());

		if (myCookBook.isEmpty() || !myCookBook.get().getIsActive()) {
			return new ResponseEntity<RESTError>(
					new RESTError(1, "There is no cookbook with " + id + " found in the database"),
					HttpStatus.NOT_FOUND);
		} else if (regularUser.isEmpty() && !regularUser.get().getIsActive()) {
			return new ResponseEntity<RESTError>(
					new RESTError(2,
							"There is no regular user with " + regularUser.get().getId() + " found in the database"),
					HttpStatus.NOT_FOUND);
		} else {
			myCookBook.get().setIsActive(false);
			cookBookRepository.save(myCookBook.get());
			return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
		}
	}

}
