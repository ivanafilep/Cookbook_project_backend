package com.praksa.team4.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.fasterxml.jackson.annotation.JsonView;
import com.praksa.team4.entities.dto.UserDTO;
import com.praksa.team4.security.Views;

public class UserCustomValidator implements Validator {
	
	@JsonView(Views.Admin.class)
	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean supports(Class<?> myClass) {
		return UserDTO.class.equals(myClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDTO user = (UserDTO) target;
		if(!user.getPassword().equals(user.getConfirmed_password())) {
	        logger.error("Users password doesn't match the confirming password");
			errors.rejectValue("confirmed_password", "Passwords must be the same");
		}
		
	}
}
