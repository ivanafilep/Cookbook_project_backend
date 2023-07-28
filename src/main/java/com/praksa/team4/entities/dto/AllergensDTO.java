package com.praksa.team4.entities.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.praksa.team4.entities.Ingredients;
import com.praksa.team4.entities.RegularUserAllergens;

public class AllergensDTO {

	@Column
	@NotNull(message = "Name must be included.")
	public String name;

	@Column
	@NotNull(message = "Icon must be included.")
	public String icon;

	@OneToOne(mappedBy = "allergen", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	public Ingredients ingredient;

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "regularUser")
	private List<RegularUserAllergens> regularUser;

	public AllergensDTO() {
		super();
	}

	public AllergensDTO(@NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Icon must be included.") String icon, Ingredients ingredient,
			List<RegularUserAllergens> regularUser) {
		super();
		this.name = name;
		this.icon = icon;
		this.ingredient = ingredient;
		this.regularUser = regularUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Ingredients getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredients ingredient) {
		this.ingredient = ingredient;
	}

}
