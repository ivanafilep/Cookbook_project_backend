package com.praksa.team4.entities.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.RegularUser;

public class RegularUserDTO {

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
	
//	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message =
//		 	"Password must be at least 8 characters long and contain a lowercase, an upercase letter and a number")
		 	@NotNull(message = "Password must be specified")
			@Size(min = 8, max = 100, message = "Password must be between {min} and {max} characters long.")
			private String password;
	
	private String confirmed_password;

	private MyCookBook myCookBook;

	private List<Allergens> allergens;

	public RegularUserDTO() {
		super();
	}

	
	public RegularUserDTO(RegularUser r) {
		super();
		this.id = r.getId();
		this.username = r.getUsername();
		this.name = r.getName();
		this.lastname = r.getLastname();
		this.email = r.getEmail();
		this.myCookBook = r.getMyCookBook();
		this.allergens = new ArrayList<>();
		this.password = r.getPassword();
		for (Allergens a : r.getAllergens()) {
			this.allergens.add(a);
		}
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

	public MyCookBook getMyCookBook() {
		return myCookBook;
	}

	public void setMyCookBook(MyCookBook myCookBook) {
		this.myCookBook = myCookBook;
	}

	public List<Allergens> getAllergens() {
		return allergens;
	}

	public void setAllergens(List<Allergens> allergens) {
		this.allergens = allergens;
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
