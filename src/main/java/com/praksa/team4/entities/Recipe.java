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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "chef")
	private Chef chef;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "myCookBook")
	private MyCookBook myCookBook;
	
	
	@OneToMany(mappedBy = "recipe", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	public List<RecipeIngredient> recipeIngredients;

	public Recipe() {
	}

	public Recipe(Integer id, @NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Steps must be included.") String steps,
			@NotNull(message = "Time must be included.") Integer time,
			@NotNull(message = "Amount must be included.") Integer amount, String picture, Chef chef,
			MyCookBook myCookBook, List<RecipeIngredient> recipeIngredients) {
		super();
		this.id = id;
		this.name = name;
		this.steps = steps;
		this.time = time;
		this.amount = amount;
		this.picture = picture;
		this.chef = chef;
		this.myCookBook = myCookBook;
		this.recipeIngredients = recipeIngredients;
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

	public MyCookBook getMyCookBook() {
		return myCookBook;
	}

	public void setMyCookBook(MyCookBook myCookBook) {
		this.myCookBook = myCookBook;
	}

	public List<RecipeIngredient> getRecipeIngredients() {
		return recipeIngredients;
	}

	public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
		this.recipeIngredients = recipeIngredients;
	}

}
