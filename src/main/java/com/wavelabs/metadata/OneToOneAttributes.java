package com.wavelabs.metadata;

public enum OneToOneAttributes {

	name("name"),cascade("cascade"), Class("class"),constrained("constrained"), embedxml("embed-xml"),
	entityname("entity-name"),fetch("fetch"),foreignkey("foreign-key"),formula("formula"),lazy("lazy"),node("node"),outerjoin("outer-join")
	,propertyref("property-ref");
	
	String oName = null;
	private OneToOneAttributes(String str)
	{
		oName=str;
	}
	public String getoName() {
		return oName;
	}
	
}
