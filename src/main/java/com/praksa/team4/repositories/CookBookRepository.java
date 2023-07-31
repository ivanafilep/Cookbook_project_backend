package com.praksa.team4.repositories;

import org.springframework.data.repository.CrudRepository;

import com.praksa.team4.entities.MyCookBook;

public interface CookBookRepository extends CrudRepository<MyCookBook, Integer> {

}
