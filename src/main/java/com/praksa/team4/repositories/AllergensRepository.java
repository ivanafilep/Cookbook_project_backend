package com.praksa.team4.repositories;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.Allergens;

public interface AllergensRepository extends CrudRepository<Allergens, Integer>{

	Optional<Allergens> findByName(String name);

}
