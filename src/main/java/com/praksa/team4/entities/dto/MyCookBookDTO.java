package com.praksa.team4.entities.dto;

import java.util.List;

import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;

public class MyCookBookDTO {

	private List<Recipe> recipes;

	public MyCookBookDTO() {
	}

	public MyCookBookDTO(MyCookBook mcb) {
		super();
		this.recipes = mcb.getRecipes();
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

}
