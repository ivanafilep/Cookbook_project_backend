package com.praksa.team4.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

	Optional<Recipe> findByName(String name);

	List<Recipe> findByChef(Chef chef);

	Optional<RegularUser> findByMyCookBook(MyCookBook myCookBook);

	ArrayList<Recipe> findAllByName(String name);
}
