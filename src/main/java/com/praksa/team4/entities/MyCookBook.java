package com.praksa.team4.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	@OneToOne(mappedBy = "myCookBook", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private RegularUser regularUser;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinTable(name = "MyCookBook_Recipes", joinColumns = {
			@JoinColumn(name = "MyCookBook_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "Recipes_id", nullable = false, updatable = false) })

	private List<Recipe> recipes;

	public MyCookBook() {
	}

	public MyCookBook(Integer id, RegularUser regularUser, List<Recipe> recipes) {
		super();
		this.id = id;
		this.regularUser = regularUser;
		this.recipes = recipes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	public RegularUser getRegularUser() {
		return regularUser;
	}

	public void setRegularUser(RegularUser regularUser) {
		this.regularUser = regularUser;
	}

}
