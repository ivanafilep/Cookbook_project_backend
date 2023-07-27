package com.praksa.team4.controllers;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.dto.IngredientsDTO;
import com.praksa.team4.repositories.IngredientsRepository;

@RestController
@RequestMapping(path = "project/ingredients")
public class IngredientsController {
	
	@Autowired
	private IngredientsRepository ingredientsRepository;
	
//	@Autowired
//	private RecipeRepository recipeRepository;
	
//  TODO CEKAMO ODGOVOR : Pretraga svih sastojaka integrisana u pisanje recepta.
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<Iterable<Ingredients>>(ingredientsRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		Optional<Ingredients> ingredient = ingredientsRepository.findById(id);
		
		if (ingredient.isPresent()) {
			return new ResponseEntity<Ingredients>(ingredient.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No ingredient found with ID "+ id, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewIngredient(@Valid @RequestBody IngredientsDTO newIngredient) {
		Ingredients ingredient = new Ingredients();

		ingredient.setName(newIngredient.getName());
		ingredient.setUnit(newIngredient.getUnit());
		ingredient.setCalories(newIngredient.getCalories());
		ingredient.setCarbs(newIngredient.getCarbs());
		ingredient.setFats(newIngredient.getFats());
		ingredient.setSugars(newIngredient.getSugars());
		ingredient.setProteins(newIngredient.getProteins());
		ingredient.setSaturatedFats(newIngredient.getSaturatedFats());
		//TODO resiti kako da dodajemo sastojcima alergene, kad cemo imati bazu sa sastojcima bez alergena (da ne bude rucno)
		//ingredient.setAllergen(newIngredient.getAllergen());

		ingredientsRepository.save(ingredient);
		return new ResponseEntity<>(ingredient, HttpStatus.CREATED);	
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateIngredient(@PathVariable Integer id, @RequestBody IngredientsDTO updatedIngredient) {
		Ingredients ingredient = ingredientsRepository.findById(id).get();

		//TODO dodati svuda kad se trazi nesto preko id-a da se getuje/updateuje/deleteuje - uslov
		if (ingredient == null) {
			return new ResponseEntity<>("No ingredient found with ID "+ id, HttpStatus.NOT_FOUND);
		}
		
		ingredient.setName(updatedIngredient.getName());
		ingredient.setUnit(updatedIngredient.getUnit());
		ingredient.setCalories(updatedIngredient.getCalories());
		ingredient.setCarbs(updatedIngredient.getCarbs());
		ingredient.setFats(updatedIngredient.getFats());
		ingredient.setSugars(updatedIngredient.getSugars());
		ingredient.setProteins(updatedIngredient.getProteins());
		ingredient.setSaturatedFats(updatedIngredient.getSaturatedFats());
		//TODO da moze alergen da se updateuje
		ingredient.setAllergen(updatedIngredient.getAllergen());

		ingredientsRepository.save(ingredient);
		return new ResponseEntity<>(ingredient, HttpStatus.OK);
	}
	
//	@RequestMapping(method = RequestMethod.DELETE, path = "/{ingredient_id}")
//	public ResponseEntity<?> deleteIngredient(@PathVariable Integer ingredient_id) {
//		Optional<Ingredients> ingredient = ingredientsRepository.findById(ingredient_id);
//		
//		if (ingredient == null) {
//			return new ResponseEntity<>("No ingredient found with ID "+ ingredient_id, HttpStatus.NOT_FOUND);
//		}
//		
//		for (Recipe recipe : recipeRepository.findAll()) {
//			if (recipe.get().getIngredients().contains(ingredient)) {
//				recipe.get().getIngredients().remove(ingredient);
//				recipeRepository.save(recipe);
//			}
//		}
//			ingredientsRepository.delete(ingredient.get());
//			return new ResponseEntity<>("Ingredient '" + ingredient.get().name + "' has been deleted successfully.", HttpStatus.OK);
//		}
//	}
	
}