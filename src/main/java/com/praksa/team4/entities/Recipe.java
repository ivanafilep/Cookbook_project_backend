package com.praksa.team4.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "recipe")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	@NotNull(message = "Name must be included.")
	public String name;

	@Column
	@NotNull(message = "Steps must be included.")
	public String steps;

	@Column
	@NotNull(message = "Time must be included.")
	public Integer time;

	@Column
	@NotNull(message = "Amount must be included.")
	public Integer amount;

	@Column
	public String picture;
	
	@Column
	private Boolean isActive;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "chef")
	private Chef chef;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "RecipeIngredient", joinColumns = {
			@JoinColumn(name = "Recipe_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "Ingredients_id", nullable = false, updatable = false) })
	public List<Ingredients> ingredients;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinTable(name = "MyCookBook_Recipes", joinColumns = {
			@JoinColumn(name = "Recipes_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "MyCookBook_id", nullable = false, updatable = false) })

	public List<MyCookBook> myCookBook;

	public Recipe() {
	}
	
	public Recipe(Integer id, @NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Steps must be included.") String steps,
			@NotNull(message = "Time must be included.") Integer time,
			@NotNull(message = "Amount must be included.") Integer amount, String picture, Boolean isActive, Chef chef,
			List<Ingredients> ingredients, List<MyCookBook> myCookBook) {
		super();
		this.id = id;
		this.name = name;
		this.steps = steps;
		this.time = time;
		this.amount = amount;
		this.picture = picture;
		this.isActive = isActive;
		this.chef = chef;
		this.ingredients = ingredients;
		this.myCookBook = myCookBook;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Chef getChef() {
		return chef;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public List<Ingredients> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredients> ingredients) {
		this.ingredients = ingredients;
	}

	public List<MyCookBook> getMyCookBook() {
		return myCookBook;
	}

	public void setMyCookBook(List<MyCookBook> myCookBook) {
		this.myCookBook = myCookBook;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
