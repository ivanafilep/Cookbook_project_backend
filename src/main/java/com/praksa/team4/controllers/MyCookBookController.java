package com.praksa.team4.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.services.MyCookBookServiceImpl;

@RestController
@RequestMapping(path = "project/cookbook")
@CrossOrigin(origins = "http://localhost:3000")
public class MyCookBookController {


	@Autowired
	private MyCookBookServiceImpl myCookBookService;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return myCookBookService.getAll();
	}

	@Secured({ "ROLE_REGULAR_USER" })
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getCookBookById(@PathVariable Integer id, Authentication authentication) {
		return myCookBookService.getCookBookById(id, authentication);
	}

	@Secured("ROLE_REGULAR_USER")
	@RequestMapping(method = RequestMethod.PUT, path = "cookbook_id/{cookbook_id}/recipe_id/{recipe_id}")
	public ResponseEntity<?> addNewRecipeToMyCookBook(@PathVariable Integer cookbook_id,
			@PathVariable Integer recipe_id, Authentication authentication) {
		return myCookBookService.addNewRecipeToMyCookBook(cookbook_id, recipe_id, authentication);
	}

	@Secured("ROLE_REGULAR_USER")
	@RequestMapping(method = RequestMethod.PUT, path = "update/cookbook_id/{cookbook_id}/recipe_id/{recipe_id}")
	public ResponseEntity<?> updateMyCookBook(@PathVariable Integer cookbook_id, @PathVariable Integer recipe_id,
			Authentication authentication) {
		return myCookBookService.updateMyCookBook(cookbook_id, recipe_id, authentication);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteMyCookBook(@PathVariable Integer id) {
		return myCookBookService.deleteMyCookBook(id);
	}
}
