package com.praksa.team4.entities.dto;

import java.util.List;

import com.praksa.team4.entities.MyCookBook;
import com.praksa.team4.entities.Recipe;

public class MyCookBookDTO {

	private Integer id;
	
	private List<Recipe> recipes;

	public MyCookBookDTO() {
	}

	

	public MyCookBookDTO(Integer id, List<Recipe> recipes) {
		super();
		this.id = id;
		this.recipes = recipes;
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
