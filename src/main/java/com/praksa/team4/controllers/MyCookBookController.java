package com.praksa.team4.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.repositories.CookBookRepository;
import com.praksa.team4.repositories.RecipeRepository;

@RestController
@RequestMapping(path = "project/cookbook")
public class MyCookBookController {

	@Autowired
	private CookBookRepository cookBookRepository;

	@Autowired
	private RecipeRepository recipeRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<Iterable<MyCookBook>>(cookBookRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getCookBookById(@PathVariable Integer id) {
		Optional<MyCookBook> myCookBook = cookBookRepository.findById(id);

		if (!myCookBook.isPresent()) {
			return new ResponseEntity<>("CookBook is not found!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(myCookBook.get(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "cookbook_id/{cookbook_id}/recipe_id/{recipe_id}")
	public ResponseEntity<?> addNewRecipeToMyCookBook(@PathVariable Integer cookbook_id,
			@PathVariable Integer recipe_id) {
		MyCookBook myCookBook = cookBookRepository.findById(cookbook_id).get();
		Recipe recipe = recipeRepository.findById(recipe_id).get();

		myCookBook.getRecipes().add(recipe);
		recipe.getMyCookBook().add(myCookBook);
		recipeRepository.save(recipe);

		cookBookRepository.save(myCookBook);

		// TODO do not duplicate recipe

		return new ResponseEntity<>(myCookBook, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "update/cookbook_id/{cookbook_id}/recipe_id/{recipe_id}")
	public ResponseEntity<?> updateMyCookBook(@PathVariable Integer cookbook_id, @PathVariable Integer recipe_id) {
		MyCookBook myCookBook = cookBookRepository.findById(cookbook_id).get();
		Recipe recipe = recipeRepository.findById(recipe_id).get();

		myCookBook.getRecipes().remove(recipe);
		cookBookRepository.save(myCookBook);

		return new ResponseEntity<>(myCookBook, HttpStatus.OK);
	}

//	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
//	public ResponseEntity<?> deleteMyCookBook(@PathVariable Integer id) {
//		MyCookBook myCookBook = cookBookRepository.findById(id).get();
//		RegularUser regularUser = recipeRepository.findByMyCookBook(myCookBook);
//		if (myCookBook == null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		} else {
//			myCookBook.setRegularUser(null);
//			cookBookRepository.save(myCookBook);
//			cookBookRepository.delete(myCookBook);
//			// TODO delete MyCookBook
//			return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
//		}
//	}
}
