package com.praksa.team4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.praksa.team4.entities.dto.EmailDTO;
import com.praksa.team4.services.EmailService;




@RestController
@RequestMapping(path = "api/v1/email")
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(method = RequestMethod.POST, path = "/message")
	public String sendSimpleMailMessage(@RequestBody EmailDTO emailDTO) {
		if (emailDTO.getTo() ==  null || emailDTO.getTo().equals("")) {
			return "Please provide a value for TO";
		}
		if (emailDTO.getSubject() ==  null || emailDTO.getSubject().equals("")) {
			return "Please provide a value for SUBJECT";
		}
		if (emailDTO.getText() ==  null || emailDTO.getText().equals("")) {
			return "Please provide a value for TEXT";
		}
		emailService.sendSimpleMessage(emailDTO);
		return "Email je uspesno poslat!";
	}

}
