package com.praksa.team4.repositories;

import org.springframework.data.repository.CrudRepository;

import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

	Recipe findByName(String name);

}
