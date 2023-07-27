package com.praksa.team4.entities.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.praksa.team4.entities.Chef;
import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.MyCookBook;

public class RecipeDTO {

	@NotNull(message = "Name must be included.")
	private String name;

	@NotNull(message = "Steps must be included.")
	public String steps;

	@NotNull(message = "Time must be included.")
	public Integer time;

	@NotNull(message = "Amount must be included.")
	public Integer amount;

	public String picture;

	private Chef chefId;

	public List<Ingredients> ingredientsId;

	private MyCookBook myCookBookId;

	public RecipeDTO() {
		super();
	}

	public RecipeDTO(@NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Steps must be included.") String steps,
			@NotNull(message = "Time must be included.") Integer time,
			@NotNull(message = "Amount must be included.") Integer amount, String picture, Chef chefId,
			List<Ingredients> ingredientsId, MyCookBook myCookBookId) {
		super();
		this.name = name;
		this.steps = steps;
		this.time = time;
		this.amount = amount;
		this.picture = picture;
		this.chefId = chefId;
		this.ingredientsId = ingredientsId;
		this.myCookBookId = myCookBookId;
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

	public List<Ingredients> getIngredientsId() {
		return ingredientsId;
	}

	public void setIngredientsId(List<Ingredients> ingredientsId) {
		this.ingredientsId = ingredientsId;
	}

	public MyCookBook getMyCookBookId() {
		return myCookBookId;
	}

	public void setMyCookBookId(MyCookBook myCookBookId) {
		this.myCookBookId = myCookBookId;
	}

}
