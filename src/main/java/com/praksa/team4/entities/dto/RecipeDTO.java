package com.praksa.team4.entities.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;

public class RecipeDTO {

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

	public List<String> ingredients;

	public RecipeDTO() {
		super();
	}

	public RecipeDTO(Recipe r) {
		super();
		this.name = r.getName();
		this.steps = r.getSteps();
		this.time = r.getTime();
		this.amount = r.getAmount();
		this.picture = r.getPicture();
		this.chefId = r.getChef();
		this.ingredients = new ArrayList<>();
		for (Ingredients i : r.getIngredients()) {
			this.ingredients.add(i.getName());
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

	public MyCookBook getMyCookBook() {
		return myCookBook;
	}

	public void setMyCookBook(MyCookBook myCookBook) {
		this.myCookBook = myCookBook;
	}

	public List<Ingredients> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredients> ingredients) {
		this.ingredients = ingredients;
	}

}
