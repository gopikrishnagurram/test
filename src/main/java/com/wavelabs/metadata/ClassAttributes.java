package com.wavelabs.metadata;

/**
 * All possible class attributes defined in ClassAttributes enum
 * 
 * @author gopikrishnag
 *
 */
public enum ClassAttributes  {

	name("name"), table("table"), lazy("lazy"), polymorphism("polymorphism"), fetch("fetch"), dynamicinsert(
			"dynamic-insert"), dynamicupdate("dynamic-update"), mutable("mutable"), where("where"), discriminatorvalue(
					"discriminatorvalue"), optimisticlock("optimistic-lock"), selectbeforeupdate(
							"select-before-update"), batchsize("batch-size"), node("node"), persister(
									"persister"), proxy("proxy"), rowid("rowid"), schema(
											"schema"), subslect("subselect"), entityname("entity-name");

	private String str = null;

	private ClassAttributes(String value) {

		str = value;
	}

	public String getString() {
		return str;
	}

}
