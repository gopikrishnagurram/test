package com.wavelabs.metadata;

public enum OneToManyAttributes  {

	Class("class"), embedxml("embed-xml"), entityname("entity-name"), node("node"), notfound("not-found");

	private String name = null;

	OneToManyAttributes(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
