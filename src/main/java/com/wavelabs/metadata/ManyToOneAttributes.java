package com.wavelabs.metadata;

/**
 * All possible many to one attributes.
 * 
 * @author gopikrishnag
 *
 */
public enum ManyToOneAttributes {

	access("access"), cascade("cascade"), Class("class"), column("column"), embedxml("embed-xml"), entityname(
			"entity-name"), fetch("fetch"), foreignkey("foreign-key"), formula("formula"), index("index"), insert(
					"insert"), lazy("lazy"), node("node"), notfound("not-found"), notnull("not-null"), optimisticlock(
							"optimistic-lock"), outerjoin("outer-join"), propertyref(
									"property-ref"), uniquekey("unique-key"), unique("unique"), update("update");

	private String name = null;

	private ManyToOneAttributes(String str) {
		name = str;
	}

	public String getName() {
		return name;
	}

}
