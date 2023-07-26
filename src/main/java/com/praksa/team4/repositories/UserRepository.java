package com.praksa.team4.repositories;

import org.springframework.data.repository.CrudRepository;

import com.praksa.team4.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

}
