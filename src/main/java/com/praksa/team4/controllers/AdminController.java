package com.praksa.team4.controllers;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.praksa.team4.entities.Admin;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.AdminRepository;

@RestController
@Secured("ROLE_ADMIN")
@RequestMapping(path = "project/admin")
public class AdminController {

	@Autowired
	private AdminRepository adminRepository;

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<Iterable<Admin>>(adminRepository.findAll(), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody UserDTO newUser) {
		Admin newAdmin = new Admin();

		newAdmin.setName(newUser.getName());
		newAdmin.setLastname(newUser.getLastname());
		newAdmin.setUsername(newUser.getUsername());
		newAdmin.setEmail(newUser.getEmail());
		newAdmin.setPassword(newUser.getPassword());
		newAdmin.setRole("ROLE_ADMIN");

		adminRepository.save(newAdmin);
		return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody UserDTO updatedAdmin) {
		Admin admin = adminRepository.findById(id).get();

		admin.setName(updatedAdmin.getName());
		admin.setLastname(updatedAdmin.getLastname());
		admin.setUsername(updatedAdmin.getUsername());
		admin.setEmail(updatedAdmin.getEmail());
		admin.setPassword(updatedAdmin.getPassword());

		adminRepository.save(admin);
		return new ResponseEntity<>(admin, HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
		Optional<Admin> admin = adminRepository.findById(id);
		if (admin.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adminRepository.delete(admin.get());
			return new ResponseEntity<>("Admin je uspesno obrisan", HttpStatus.OK);
		}
	}
}
