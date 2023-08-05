package com.praksa.team4.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.Ingredients;

public interface IngredientsRepository extends CrudRepository<Ingredients, Integer> {

	Optional<Ingredients> findByName(String name);

}
