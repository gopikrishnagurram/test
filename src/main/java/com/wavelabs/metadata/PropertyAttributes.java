package com.wavelabs.metadata;

public enum PropertyAttributes {
	access("access"), notnull("not-null"), insert("insert"), unique("unique"), uniquekey("unique-key"), type(
			"type"), scale("scale"), precision("precision"), optimisticlock("optimistic-lock"), node("node"), length(
					"length"), lazy("lazy"), index("index"), generated(
							"generated"), formula("formula"), update("update"), column("column"), name("name");
	private String ename = null;

	private PropertyAttributes(String str) {
		ename = str;
	}

	public String getEname() {
		return ename;
	}

}
