package com.praksa.team4.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.RegularUser;

public interface AllergensRepository extends CrudRepository<Allergens, Integer> {

	Optional<Allergens> findByName(String name);

	List<Allergens> findAllByRegularUsers(RegularUser regularUser);

}
