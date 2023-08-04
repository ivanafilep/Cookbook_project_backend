package com.praksa.team4.entities.dto;

import java.util.List;
import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.RegularUser;

public class MyCookBookDTO {
	
	private RegularUser regularUser;

	private List<Recipe> recipes;

	public MyCookBookDTO() {
	}

	public MyCookBookDTO(RegularUser regularUser, List<Recipe> recipes) {
		super();
		this.regularUser = regularUser;
		this.recipes = recipes;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	public RegularUser getRegularUser() {
		return regularUser;
	}

	public void setRegularUser(RegularUser regularUser) {
		this.regularUser = regularUser;
	}

}
