package com.praksa.team4.entities.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.Recipe;

public class RecipeIdAmountDTO {

	private Integer id;
	
	@NotNull(message = "Name must be included.")
	public String name;

	@NotNull(message = "Steps must be included.")
	public String steps;

	@NotNull(message = "Time must be included.")
	public Integer time;

	@NotNull(message = "Amount must be included.")
	public Integer amount;

	public String picture;

	private Chef chefId;

	private Map<Integer, Integer> ingredientIdAmounts;

	public RecipeIdAmountDTO() {
		super();
	}

	public RecipeIdAmountDTO(Recipe r) {
		super();
		this.id =r.getId();
		this.name = r.getName();
		this.steps = r.getSteps();
		this.time = r.getTime();
		this.amount = r.getAmount();
		this.picture = r.getPicture();
		this.chefId = r.getChef();
		for (Ingredients ingredients : r.getIngredients()) {
			ingredientIdAmounts.put(ingredients.getId(), ingredients.getAmount());
		}
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

	public Chef getChefId() {
		return chefId;
	}

	public void setChefId(Chef chefId) {
		this.chefId = chefId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<Integer, Integer> getIngredientIdAmounts() {
		return ingredientIdAmounts;
	}

	public void setIngredientIdAmounts(Map<Integer, Integer> ingredientIdAmounts) {
		this.ingredientIdAmounts = ingredientIdAmounts;
	}

}
