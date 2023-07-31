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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "allergens")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Allergens {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column
	@NotNull(message = "Name must be included.")
	private String name;

	@Column
	@NotNull(message = "Icon must be included.")
	private String icon;

	// TODO static folder urls

	@OneToMany(mappedBy = "allergen", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Ingredients> ingredient;

	// manytoone
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinTable(name = "RegularUserAllergens", joinColumns = {
			@JoinColumn(name = "Allergens_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "RegularUser_id", nullable = false, updatable = false) })

	private RegularUser regularUsers;

	public Allergens(Integer id, @NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Icon must be included.") String icon, List<Ingredients> ingredient,
			RegularUser regularUsers) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.ingredient = ingredient;
		this.regularUsers = regularUsers;
	}

	public Allergens() {
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

	public RegularUser getRegularUsers() {
		return regularUsers;
	}

	public void setRegularUsers(RegularUser regularUsers) {
		this.regularUsers = regularUsers;
	}
}
