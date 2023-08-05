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

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@Column
	private Boolean isActive;
	
	// TODO static folder urls

	@JsonIgnore
	@OneToMany(mappedBy = "allergen", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Ingredients> ingredient;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinTable(name = "RegularUserAllergens", joinColumns = {
			@JoinColumn(name = "Allergens_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "RegularUser_id", nullable = false, updatable = false) })

	private List<RegularUser> regularUsers;

	public Allergens(Integer id, @NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Icon must be included.") String icon, Boolean isActive, List<Ingredients> ingredient,
			List<RegularUser> regularUsers) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.isActive = isActive;
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

	public List<RegularUser> getRegularUsers() {
		return regularUsers;
	}

	public void setRegularUsers(List<RegularUser> regularUsers) {
		this.regularUsers = regularUsers;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
