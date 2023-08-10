package com.praksa.team4.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.UserEntity;
import com.praksa.team4.entities.dto.EmailDTO;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());


	@Override
	public void sendSimpleMessage(EmailDTO email) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(email.getTo());
		message.setSubject(email.getSubject());
		message.setText(email.getText());
		
		emailSender.send(message);
	}

public void messageToAdmin(Chef chef, Recipe recipe, UserEntity admin) {
		
	EmailDTO email = new EmailDTO();

    email.setTo(admin.getEmail());
    email.setTo("teodora.romic.brains22@gmail.com");
    logger.info("Setting up admin's email address");

    email.setSubject("New Recipe Added - " + recipe.getName());
    logger.info("Setting up recipe name in the email subject");

    String text = "One of your chefs, " + chef.getName() + " " + chef.getLastname() +
            ", has added a new recipe to your site:\n" +
            "Recipe Name: " + recipe.getName() + "\n" +
            "Ingredients: " + recipe.getIngredients() + "\n" +
            "Steps: " + recipe.getSteps();
    logger.info("Setting up the email message with information about the new recipe");

    email.setText(text);

    sendSimpleMessage(email);
	}
}
