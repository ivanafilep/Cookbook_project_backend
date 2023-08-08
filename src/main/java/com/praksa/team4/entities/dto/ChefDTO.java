package com.praksa.team4.entities.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Recipe;

public class ChefDTO {
	
	private Integer id;
	
	@NotNull(message = "Username must be specified")
	@Size(min = 2, max = 30, message = "User name must be between {min} and {max} characters long.")
	private String username;
		
	@NotNull(message = "Name must be included.")
	@Size(min = 2, max = 30, message = "Name must be between {min} and {max} characters long.")
	private String name;

	@NotNull(message = "Lastname must be included.")
	@Size(min = 2, max = 30, message = "Lastname must be between {min} and {max} characters long.")
	private String lastname;

	@NotNull(message = "Email must be included.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.")
	private String email;
	
//	@Pattern(regexp = "^(?=.[0-9])(?=.[a-z])(?=.*[A-Z]).{8,100}$", message =
//		 	"Password must be at least 8 characters long and contain a lowercase, an upercase letter and a number")
		 	@NotNull(message = "Password must be specified")
			@Size(min = 8, max = 100, message = "Password must be between {min} and {max} characters long.")
			private String password;
	
	private String confirmed_password;
	
	public List<Recipe> recipes;

	public ChefDTO() {
		super();
	}

	public ChefDTO(Chef c) {
		super();
		this.id = c.getId();
		this.username = c.getUsername();
		this.name = c.getName();
		this.lastname = c.getLastname();
		this.email = c.getEmail();
		this.recipes = c.getRecipes();
		this.password = c.getPassword();
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmed_password() {
		return confirmed_password;
	}

	public void setConfirmed_password(String confirmed_password) {
		this.confirmed_password = confirmed_password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
