package com.praksa.team4.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "regular_user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RegularUser extends UserEntity {

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "myCookBook")
	private MyCookBook myCookBook;

	
	@OneToMany
    @JoinTable(
        name = "user_allergen",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
	//@OneToMany(mappedBy = "regularUsers", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Allergens> allergens;

	public RegularUser() {
		super();
	}

	public RegularUser(MyCookBook myCookBook, List<Allergens> allergens) {
		super();
		this.myCookBook = myCookBook;
		this.allergens = allergens;
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

}
