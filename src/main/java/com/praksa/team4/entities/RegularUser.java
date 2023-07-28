package com.praksa.team4.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
	
	@OneToMany(mappedBy = "regularUser", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<RegularUserAllergens> myAllergens = new ArrayList<RegularUserAllergens>();

	public RegularUser() {
		super();
	}

	public RegularUser(Integer id, String username, String password, String name, String lastname, String email,
			String role, Integer version, MyCookBook myCookBook, List<RegularUserAllergens> myAllergens) {
		super(id, username, password, name, lastname, email, role, version);
		this.myCookBook = myCookBook;
		this.myAllergens = myAllergens;
	}

	public MyCookBook getMyCookBook() {
		return myCookBook;
	}

	public void setMyCookBook(MyCookBook myCookBook) {
		this.myCookBook = myCookBook;
	}

	public List<RegularUserAllergens> getMyAllergens() {
		return myAllergens;
	}

	public void setMyAllergens(List<RegularUserAllergens> myAllergens) {
		this.myAllergens = myAllergens;
	}

}
