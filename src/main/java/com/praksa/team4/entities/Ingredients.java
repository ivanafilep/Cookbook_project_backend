package com.praksa.team4.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ingredients")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Ingredients {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
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
	@JoinColumn(name = "allergens")
	public Allergens allergens;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe")
	public Recipe recipe;

	public Ingredients() {}
	
	public Ingredients(Integer id, @NotNull(message = "Unit must be included.") String unit,
			@NotNull(message = "Calories must be included.") Float calories, Float carbs, Float fats, Float sugars,
			Float proteins, Float saturatedFats, Allergens allergens, Recipe recipe) {
		super();
		this.id = id;
		this.unit = unit;
		this.calories = calories;
		this.carbs = carbs;
		this.fats = fats;
		this.sugars = sugars;
		this.proteins = proteins;
		this.saturatedFats = saturatedFats;
		this.allergens = allergens;
		this.recipe = recipe;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Allergens getAllergens() {
		return allergens;
	}

	public void setAllergens(Allergens allergens) {
		this.allergens = allergens;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

}
