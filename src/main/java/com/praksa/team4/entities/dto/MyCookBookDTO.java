package com.praksa.team4.entities.dto;

import java.util.List;

import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;

public class MyCookBookDTO {

	private Integer id;
	
	private List<Recipe> recipes;

	public MyCookBookDTO() {
	}

	

	public MyCookBookDTO(MyCookBook mcb) {
		super();
		this.id = mcb.getId();
		this.recipes = mcb.getRecipes();
	}



	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}

}
