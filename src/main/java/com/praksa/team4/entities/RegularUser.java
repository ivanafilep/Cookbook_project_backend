package com.praksa.team4.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "regular_user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RegularUser extends UserEntity {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "myCookBook")
	private MyCookBook myCookBook;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinTable(name = "RegularUserAllergens", joinColumns = {
			@JoinColumn(name = "RegularUser_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "Allergens_id", nullable = false, updatable = false) })
	private List<Allergens> allergens;
	
	private Boolean isActive;

	public RegularUser() {
		super();
	}

	public RegularUser(Integer id,
			@NotNull(message = "Username must be specified") @Size(min = 2, max = 30, message = "User name must be between {min} and {max} characters long.") String username,
			@NotNull(message = "Password must be specified") @Size(min = 8, max = 100, message = "Password must be between {min} and {max} characters long.") String password,
			@NotNull(message = "Name must be included.") @Size(min = 2, max = 30, message = "Name must be between {min} and {max} characters long.") String name,
			@NotNull(message = "Lastname must be included.") @Size(min = 2, max = 30, message = "Lastname must be between {min} and {max} characters long.") String lastname,
			@NotNull(message = "Email must be included.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email,
			String role, Integer version, MyCookBook myCookBook, List<Allergens> allergens, Boolean isActive) {
		super(id, username, password, name, lastname, email, role, version);
		this.myCookBook = myCookBook;
		this.allergens = allergens;
		this.isActive = isActive;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
