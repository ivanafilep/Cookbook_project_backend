package com.praksa.team4.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.RegularUser;

public interface MyCookBookRepository  extends CrudRepository<MyCookBook, Integer>{

	Optional<MyCookBook> findByRegularUser(RegularUser regularUser);

}
