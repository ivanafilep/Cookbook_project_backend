package com.praksa.team4.repositories;

import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.Ingredients;

public interface IngredientsRepository extends CrudRepository<Ingredients, Integer> {

	Ingredients findByName(String name);

}
