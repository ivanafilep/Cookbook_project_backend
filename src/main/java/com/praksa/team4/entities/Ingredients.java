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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ingredients")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Ingredients {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	@NotNull(message = "Name must be included.")
	public String name;

	@Column
	@NotNull(message = "Unit must be included.")
	public String unit;

	@Column
	@NotNull(message = "Calories must be included.")
	public Float calories;

	@Column
	public Float carbs;

	@Column
	public Float fats;

	@Column
	public Float sugars;

	@Column
	public Float proteins;

	@Column
	public Float saturatedFats;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "allergen")
	public Allergens allergen;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinTable(name = "RecipeIngredient", joinColumns = {
			@JoinColumn(name = "Ingredients_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "Recipe_id", nullable = false, updatable = false) })

	public List<Recipe> recipes;

	public Ingredients() {
	}

	public Ingredients(Integer id, @NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Unit must be included.") String unit,
			@NotNull(message = "Calories must be included.") Float calories, Float carbs, Float fats, Float sugars,
			Float proteins, Float saturatedFats, Allergens allergen, List<Recipe> recipes) {
		super();
		this.id = id;
		this.name = name;
		this.unit = unit;
		this.calories = calories;
		this.carbs = carbs;
		this.fats = fats;
		this.sugars = sugars;
		this.proteins = proteins;
		this.saturatedFats = saturatedFats;
		this.allergen = allergen;
		this.recipes = recipes;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Float getCalories() {
		return calories;
	}

	public void setCalories(Float calories) {
		this.calories = calories;
	}

	public Float getCarbs() {
		return carbs;
	}

	public void setCarbs(Float carbs) {
		this.carbs = carbs;
	}

	public Float getFats() {
		return fats;
	}

	public void setFats(Float fats) {
		this.fats = fats;
	}

	public Float getSugars() {
		return sugars;
	}

	public void setSugars(Float sugars) {
		this.sugars = sugars;
	}

	public Float getProteins() {
		return proteins;
	}

	public void setProteins(Float proteins) {
		this.proteins = proteins;
	}

	public Float getSaturatedFats() {
		return saturatedFats;
	}

	public void setSaturatedFats(Float saturatedFats) {
		this.saturatedFats = saturatedFats;
	}

	public Allergens getAllergen() {
		return allergen;
	}

	public void setAllergen(Allergens allergen) {
		this.allergen = allergen;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

}
