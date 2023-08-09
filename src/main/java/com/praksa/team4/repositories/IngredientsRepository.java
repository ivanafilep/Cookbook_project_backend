package com.praksa.team4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;

public interface IngredientsRepository extends CrudRepository<Ingredients, Integer> {

	Optional<Ingredients> findByName(String name);

	List<Ingredients> findAllByRecipes(Recipe recipe);

}
