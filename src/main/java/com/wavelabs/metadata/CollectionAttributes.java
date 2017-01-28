package com.wavelabs.metadata;

/**
 * All possible collection attributes defined in CollectionAttributes enum.
 * 
 * @author gopikrishnag
 *
 */

public enum CollectionAttributes {

	cascade("cascade"), inverse("inverse"), lazy("lazy"), fetch("fetch"), batchsize("batch-size"), access(
			"access"), catalog("catalog"), check("check"), collectiontype("collection-type"), embedxml(
					"embed-xml"), mutable("mutable"), node("node"), optimisticlock("optimistic-lock"), orderby(
							"order-by"), outerjoin("outer-join"), persister("persister"), schema(
									"schema"), sort("sort"), subselect("subselect"), table("table"), where("where");
	private String name = null;

	CollectionAttributes(String str) {
		name = str;
	}

	public String getName() {
		return name;
	}
}
