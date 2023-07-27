package com.praksa.team4.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.praksa.team4.entities.RegularUser;

public interface RegularUserRepository extends CrudRepository<RegularUser, Integer>{

	Optional<RegularUser> findByName(String name);
}
