package com.praksa.team4.entities.dto;

import javax.validation.constraints.NotNull;

public class AllergensDTO {

	@NotNull(message = "Name must be included.")
	public String name;

	@NotNull(message = "Icon must be included.")
	public String icon;

	public AllergensDTO() {
		super();
	}

	public AllergensDTO(@NotNull(message = "Name must be included.") String name,
			@NotNull(message = "Icon must be included.") String icon) {
		super();
		this.name = name;
		this.icon = icon;
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

}
