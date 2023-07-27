package com.praksa.team4.controllers;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.dto.AllergensDTO;
import com.praksa.team4.repositories.AllergensRepository;

@RestController
@RequestMapping(path = "project/allergens")
public class AllergensController {
	@Autowired
	private AllergensRepository allergensRepository;
	
	//@Autowired
	//private IngredientsRepository ingredientsRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<Iterable<Allergens>>(allergensRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAllergen(@Valid @RequestBody AllergensDTO allergen) {
		Allergens newAllergen = new Allergens();
		
		newAllergen.setName(allergen.getName());
		newAllergen.setIcon(allergen.getIcon());
		
		allergensRepository.save(newAllergen);
		return new ResponseEntity<>(newAllergen, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateAllergen(@PathVariable Integer id, @RequestBody AllergensDTO updatedAllergen) {
		Allergens allergen = allergensRepository.findById(id).get();

		allergen.setName(updatedAllergen.getName());
		allergen.setIcon(updatedAllergen.getIcon());
		

		allergensRepository.save(allergen);
		return new ResponseEntity<>(allergen, HttpStatus.OK);
	}
	
	/*
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteAllergen(@PathVariable Integer id) {
		Optional<Allergens> allergen = allergensRepository.findById(id);
		if (allergen.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			
			if (!allergen.get().getIngredient().isEmpty()) {
				for (Ingredients ingredient : allergen.get().getIngredient()) {
					ingredient.setAllergen(null);
					ingredientsRepository.save(ingredient);
				}
			}
			
			allergensRepository.delete(allergen.get());
			return new ResponseEntity<>("Allergen has been successfully deleted", HttpStatus.OK);
		}
		*/
		
	}
	
	
	


