package com.praksa.team4.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.RegularUser;
import com.praksa.team4.entities.RegularUserAllergens;
import com.praksa.team4.repositories.AllergensRepository;
import com.praksa.team4.repositories.RegularUserAllergensRepository;
import com.praksa.team4.repositories.RegularUserRepository;

@Service
public class RegularUserAllergensServiceImpl implements RegularUserAllergensService {

	@Autowired
	private RegularUserRepository regularUserRepository;

	@Autowired
	private AllergensRepository allergensRepository;

	@Autowired
	private RegularUserAllergensRepository regularUserAllergensRepository;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> addAllergensToRegularUser(@PathVariable Integer regularUserId,
			@PathVariable Integer allergensId) {

		Optional<RegularUser> regularUser = regularUserRepository.findById(regularUserId);
		Optional<Allergens> allergens = allergensRepository.findById(allergensId);

		if (!regularUser.isPresent() || !allergens.isPresent()) {
			return new ResponseEntity<>("Allergens or regularUser are not found.", HttpStatus.NOT_FOUND);
		}

		RegularUserAllergens regularUserAllergens = new RegularUserAllergens();
		regularUserAllergens.setRegularUser(regularUser.get());
		regularUserAllergens.setAllergens(allergens.get());
		regularUserAllergensRepository.save(regularUserAllergens);

		logger.info("Allergens with ID: {}, is succesfully added to regularUser with ID: {}", regularUserId,
				allergensId);
		return new ResponseEntity<>("Allergens are succesfully added to regularUser", HttpStatus.OK);
	}
}
