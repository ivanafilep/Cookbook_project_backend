package com.praksa.team4.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "regular_user")
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class RegularUser extends UserEntity {

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "myCookBook")
	private MyCookBook myCookBook;
	
	//TODO kako uraditi alergene ovde
	
//	@OneToMany(mappedBy = "regularUser", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//	private List<String> myAllergens;

	public RegularUser() {
		super();
	}

	public RegularUser(Integer id,
			@NotNull(message = "Username must be specified") @Size(min = 2, max = 30, message = "User name must be between {min} and {max} characters long.") String username,
			@NotNull(message = "Password must be specified") @Size(min = 8, max = 100, message = "Password must be between {min} and {max} characters long.") String password,
			@NotNull(message = "Name must be included.") @Size(min = 2, max = 30, message = "Name must be between {min} and {max} characters long.") String name,
			@NotNull(message = "Lastname must be included.") @Size(min = 2, max = 30, message = "Lastname must be between {min} and {max} characters long.") String lastname,
			@NotNull(message = "Email must be included.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email,
			String role, Integer version) {
		super(id, username, password, name, lastname, email, role, version);
	}

	public RegularUser(MyCookBook myCookBook, List<Allergens> myAllergens) {
		super();
		this.myCookBook = myCookBook;
		//this.myAllergens = myAllergens;
	}

	public MyCookBook getMyCookBook() {
		return myCookBook;
	}

	public void setMyCookBook(MyCookBook myCookBook) {
		this.myCookBook = myCookBook;
	}

//	public List<Allergens> getMyAllergens() {
//		return myAllergens;
//	}
//
//	public void setMyAllergens(List<Allergens> myAllergens) {
//		this.myAllergens = myAllergens;
//	}
	
}
