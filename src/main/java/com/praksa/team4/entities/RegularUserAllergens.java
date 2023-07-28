package com.praksa.team4.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class RegularUserAllergens {

	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "allergens")
	private Allergens allergens;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "regularUser")
	private RegularUser regularUser;

	public RegularUserAllergens() {
		super();
	}

	public RegularUserAllergens(Integer id, Allergens allergens, RegularUser regularUser) {
		super();
		this.id = id;
		this.allergens = allergens;
		this.regularUser = regularUser;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Allergens getAllergens() {
		return allergens;
	}

	public void setAllergens(Allergens allergens) {
		this.allergens = allergens;
	}

	public RegularUser getRegularUser() {
		return regularUser;
	}

	public void setRegularUser(RegularUser regularUser) {
		this.regularUser = regularUser;
	}
	
}
