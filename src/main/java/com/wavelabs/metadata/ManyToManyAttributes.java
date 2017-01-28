package com.wavelabs.metadata;

public enum ManyToManyAttributes {

	Class("class"),column("column"),embedxml("embed-xml"),entityname("entity-name"),
	fetch("fetch"),foreignkey("foreign-key"),formula("formula"),lazy("lazy"),node("node"),
	notfound("not-found"),orderby("order-by"),outerjoin("outer-join"),propertyref("property-ref"),
	unique("unique"),where("where");

	private String name = null;

	private ManyToManyAttributes(String str) {
		name = str;
	}

	public String getName() {
		return name;
	}

}
