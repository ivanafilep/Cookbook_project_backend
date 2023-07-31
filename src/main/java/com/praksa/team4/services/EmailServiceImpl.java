package com.praksa.team4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.praksa.team4.entities.dto.EmailDTO;


@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	public JavaMailSender emailSender;
	
	@Override
	public void sendSimpleMessage(EmailDTO emailDTO) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailDTO.getTo());
		message.setSubject(emailDTO.getSubject());
		message.setText(emailDTO.getText());
		emailSender.send(message);
	}
	

}
