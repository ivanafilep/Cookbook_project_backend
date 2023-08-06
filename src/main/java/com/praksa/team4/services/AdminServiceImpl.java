package com.praksa.team4.services;

import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.praksa.team4.entities.Admin;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.repositories.AdminRepository;
import com.praksa.team4.util.UserCustomValidator;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	UserCustomValidator userValidator;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

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

	public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody UserDTO updatedAdmin) {
		Optional<Admin> admin = adminRepository.findById(id);

		if (admin.isEmpty()) {
			return new ResponseEntity<>("Chef not found in the database", HttpStatus.NOT_FOUND);
		}

		admin.get().setName(updatedAdmin.getName());
		admin.get().setLastname(updatedAdmin.getLastname());
		admin.get().setUsername(updatedAdmin.getUsername());
		admin.get().setEmail(updatedAdmin.getEmail());
		admin.get().setPassword(updatedAdmin.getPassword());

		adminRepository.save(admin.get());
		return new ResponseEntity<>(admin.get(), HttpStatus.OK);
	}

	public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
		Optional<Admin> admin = adminRepository.findById(id);

		if (admin.isEmpty()) {
			return new ResponseEntity<>("Chef not found in the database", HttpStatus.NOT_FOUND);
		} else {
			adminRepository.delete(admin.get());
			return new ResponseEntity<>("Admin je uspesno obrisan", HttpStatus.OK);
		}
	}

}
