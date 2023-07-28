package com.praksa.team4.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "my_cookbook")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class MyCookBook {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JsonIgnore
	@OneToMany(mappedBy = "myCookBook", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	public List<Recipe> myRecipes;

	@OneToOne(mappedBy = "myCookBook", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private RegularUser regularUser;

	public MyCookBook() {
	}

	public MyCookBook(Integer id, List<Recipe> myRecipes, RegularUser regularUser) {
		super();
		this.id = id;
		this.myRecipes = myRecipes;
		this.regularUser = regularUser;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Recipe> getMyRecipes() {
		return myRecipes;
	}

	public void setMyRecipes(List<Recipe> myRecipes) {
		this.myRecipes = myRecipes;
	}

	public RegularUser getRegularUser() {
		return regularUser;
	}

	public void setRegularUser(RegularUser regularUser) {
		this.regularUser = regularUser;
	}

}
