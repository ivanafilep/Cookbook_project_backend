package com.praksa.team4.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "allergens")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Allergens {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	@NotNull(message = "Name must be included.")
	public String name;
	
	@Column
	@NotNull(message = "Icon must be included.")
	public String icon;
	
	@OneToMany(mappedBy = "allergen", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	public List<Ingredients> ingredient;

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "regularUser")
	private List<RegularUserAllergens> regularUser;
	
	public Allergens() {}

	public Allergens(Integer id, @NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Icon must be included.") String icon, List<Ingredients> ingredient,
			List<RegularUserAllergens> regularUser) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.ingredient = ingredient;
		this.regularUser = regularUser;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<Ingredients> getIngredient() {
		return ingredient;
	}

	public void setIngredient(List<Ingredients> ingredient) {
		this.ingredient = ingredient;
	}

	public List<RegularUserAllergens> getRegularUser() {
		return regularUser;
	}

	public void setRegularUser(List<RegularUserAllergens> regularUser) {
		this.regularUser = regularUser;
	}
	
}
