package com.praksa.team4.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.CookBookRepository;
import com.praksa.team4.repositories.RegularUserRepository;

@RestController
@RequestMapping(path = "project/regularuser")
public class RegularUserController {

	@Autowired
	private RegularUserRepository regularUserRepository;

	@Autowired
	private CookBookRepository cookBookRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<Iterable<RegularUser>>(regularUserRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewRegularUser(@Valid @RequestBody UserDTO newUser) {
		RegularUser newRegularUser = new RegularUser();

		newRegularUser.setName(newUser.getName());
		newRegularUser.setLastname(newUser.getLastname());
		newRegularUser.setUsername(newUser.getUsername());
		newRegularUser.setEmail(newUser.getEmail());
		newRegularUser.setPassword(newUser.getPassword());
		newRegularUser.setRole("REGULAR_USER");

		regularUserRepository.save(newRegularUser);

		MyCookBook myCookBook = new MyCookBook();
		myCookBook.setRegularUser(newRegularUser);
		logger.info("My cookbook" + myCookBook);
		newRegularUser.setMyCookBook(myCookBook);

		cookBookRepository.save(myCookBook);
		regularUserRepository.save(newRegularUser);
		return new ResponseEntity<>(newRegularUser, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateRegularUser(@PathVariable Integer id, @RequestBody UserDTO updatedRegularUser) {
		RegularUser regularUser = regularUserRepository.findById(id).get();

		regularUser.setName(updatedRegularUser.getName());
		regularUser.setLastname(updatedRegularUser.getLastname());
		regularUser.setUsername(updatedRegularUser.getUsername());
		regularUser.setEmail(updatedRegularUser.getEmail());
		regularUser.setPassword(updatedRegularUser.getPassword());
		regularUserRepository.save(regularUser);
		return new ResponseEntity<>(regularUser, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRegularUser(@PathVariable Integer id) {
		Optional<RegularUser> regularUser = regularUserRepository.findById(id);
		// TODO delete also MyCookBook
		if (regularUser.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			regularUserRepository.delete(regularUser.get());
			return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRegularUserById(@PathVariable Integer id) {
		Optional<RegularUser> regularUser = regularUserRepository.findById(id);

		if (!regularUser.isPresent()) {
			return new ResponseEntity<>("Regular user not found!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(regularUser.get(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/by_name")
	public ResponseEntity<?> getRegularUserByName(@RequestParam String name) {
		Optional<RegularUser> regularUser = regularUserRepository.findByName(name);

		if (!regularUser.isPresent()) {
			return new ResponseEntity<>("Regular user not found!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(regularUser.get(), HttpStatus.OK);
	}
}
