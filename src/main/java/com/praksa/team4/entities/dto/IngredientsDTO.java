package com.praksa.team4.entities.dto;

import javax.validation.constraints.NotNull;
import com.praksa.team4.entities.Allergens;
import com.praksa.team4.entities.Ingredients;

public class IngredientsDTO {

	private Integer id;
	
	@NotNull(message = "Name must be included.")
	public String name;

	@NotNull(message = "Unit must be included.")
	public String unit;

	@NotNull(message = "Calories must be included.")
	public Float calories;

	public Float carbs;

	public Float fats;

	public Float sugars;

	public Float proteins;

	public Float saturatedFats;

	public Allergens allergen;

	public IngredientsDTO() {
	}

	

	public IngredientsDTO(Ingredients i) {
		super();
		this.id = i.getId();
		this.name = i.getName();
		this.unit = i.getUnit();
		this.calories = i.getCalories();
		this.carbs = i.getCarbs();
		this.fats = i.getFats();
		this.sugars = i.getSugars();
		this.proteins = i.getProteins();
		this.saturatedFats = i.getSaturatedFats();
		this.allergen = i.getAllergen();
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



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}

}
