package com.praksa.team4.entities.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.Recipe;

public class IngredientsDTO {

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

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "allergen")
	public Allergens allergen;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe")
	public Recipe recipe;

	public IngredientsDTO() {
	}

	public IngredientsDTO(@NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Unit must be included.") String unit,
			@NotNull(message = "Calories must be included.") Float calories, Float carbs, Float fats, Float sugars,
			Float proteins, Float saturatedFats, Allergens allergen, Recipe recipe) {
		super();
		this.name = name;
		this.unit = unit;
		this.calories = calories;
		this.carbs = carbs;
		this.fats = fats;
		this.sugars = sugars;
		this.proteins = proteins;
		this.saturatedFats = saturatedFats;
		this.allergen = allergen;
		this.recipe = recipe;
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

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

}
