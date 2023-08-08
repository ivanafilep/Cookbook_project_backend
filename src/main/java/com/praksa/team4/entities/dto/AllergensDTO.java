package com.praksa.team4.entities.dto;

import javax.validation.constraints.NotNull;
import com.praksa.team4.entities.Allergens;

public class AllergensDTO {

	private Integer id;
	
	@NotNull(message = "Name must be included.")
	public String name;

	@NotNull(message = "Icon must be included.")
	public String icon;

	public AllergensDTO() {
		super();
	}

	public AllergensDTO(Allergens a) {
		super();
		this.id = a.getId();
		this.name = a.getName();
		this.icon = a.getIcon();
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
