package com.wavelabs.metadata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.hibernate.internal.util.xml.XmlDocument;

/**
 * Provides the methods to read every property attributes, collection
 * attributes, relationship attributes.
 * 
 * @author gopikrishnag
 */

public class HbmFileMetaData {

	private XmlDocument xmlDocuemnt = null;
	private Element startElement = null;
	private Element classElement = null;
	private Element idElement = null;
	private List<Element> queryElement = null;
	private List<Element> sqlQueryElement = null;
	private List<Element> filterElement = null;
	private List<Element> filterDefElement = null;
	private List<Element> unionSubClass = new ArrayList<Element>();
	private List<Element> joinedSubClass = new ArrayList<Element>();
	private List<Element> subClass = new ArrayList<Element>();
	private Map<String, Element> unionClassMap = new HashMap<String, Element>();
	private Map<String, Element> joinedClassMap = new HashMap<String, Element>();
	private Map<String, Element> subClassMap = new HashMap<String, Element>();
	private List<Element> setElement = null;
	private List<Element> listElement = null;
	private List<Element> mapElement = null;
	private List<Element> bagElement = null;
	private List<Element> idBagElement = null;
	private List<Element> propertyElements = null;
	private List<Element> manyToOneElement = null;
	private List<Element> oneToOneElements = null;
	private Map<String, Element> manyToOneMap = new HashMap<String, Element>();
	private Map<String, Element> propertyMap = new HashMap<String, Element>();
	private Map<String, Element> filterMap = new HashMap<String, Element>();
	private Map<String, Element> filterDefMap = new HashMap<String, Element>();
	private Map<String, Element> duplicateMap = new HashMap<String, Element>();
	private Map<String, Element> duplicateMap2 = new HashMap<String, Element>();
	private Map<String, Map<String, Element>> filterParamMap = new HashMap<String, Map<String, Element>>();
	private Map<String, Element> queryMap = new HashMap<String, Element>();
	private Map<String, Element> sqlQueryMap = new HashMap<String, Element>();
	private Map<String, Map<String, Element>> sqlQueryParamMap = new HashMap<String, Map<String, Element>>();
	private Map<String, Element> oneToOneMap= new HashMap<String, Element>();
	@SuppressWarnings("unchecked")
	public HbmFileMetaData(XmlDocument xmd, SessionFactory factory) {
		try {
			xmlDocuemnt = xmd;
			startElement = xmlDocuemnt.getDocumentTree().getRootElement();
			classElement = startElement.element("class");
			isSessionFactoryBuilt(factory);
			unionSubClass = startElement.elements("union-subclass");
			filterElement = classElement.elements("filter");
			queryElement = startElement.elements("query");
			sqlQueryElement = startElement.elements("sql-query");
			filterDefElement = startElement.elements("filter-def");
			subClass = startElement.elements("subclass");
			joinedSubClass = startElement.elements("joined-subclass");
			idElement = classElement.element("id");
			propertyElements = classElement.elements("property");
			setElement = classElement.elements("set");
			listElement = classElement.elements("list");
			mapElement = classElement.elements("map");
			bagElement = classElement.elements("bag");
			idBagElement = classElement.elements("idbag");
			manyToOneElement = classElement.elements("many-to-one");
			oneToOneElements = classElement.elements("one-to-one");
			if (propertyElements.size() > 0)
				propertyMap=mapBuilder(propertyElements);
			if (manyToOneElement.size() > 0)
				manyToOneMap = mapBuilder(manyToOneElement);
			if(oneToOneElements.size()>0)
				oneToOneMap=mapBuilder(oneToOneElements);
			if (unionSubClass.size() > 0)
				unionClassMap=mapBuilder(unionSubClass);
			if (subClass.size() > 0)
				subClassMap=mapBuilder(subClass);
			if (joinedSubClass.size() > 0)
				joinedClassMap=mapBuilder(joinedSubClass);
			if (filterElement.size() > 0)
				filterMap=mapBuilder(filterElement);
			if (filterDefElement.size() > 0)
				filterDefMapBuilder();
			if (queryElement.size() > 0)
				queryMap=mapBuilder(queryElement);
			if (sqlQueryElement.size() > 0)
				SqlQueryMapBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void isSessionFactoryBuilt(SessionFactory factory) {
		if (factory instanceof SessionFactory) {
			String name = null;
			String entityName = classElement.attributeValue("entity-name");
			String className = this.getMappedClass();
			name = (entityName == null ? className : entityName);
			try {
				factory.getClassMetadata(name).getPropertyNullability();
			} catch (Exception e) {
				throw new NullPointerException(this.getMappedClass() + " don't have hbm file in cfg file");
			}

			System.out.println("Schema exported completed");
		}

		else {
			throw new NullPointerException("SessionFactory object not created");
		}
	}

	private Map<String ,Element> mapBuilder(List<Element> source)
	{
		Map<String, Element> map = new HashMap<String, Element>();
		for(Element e : source)
			map.put(e.attributeValue("name"), e);
		return map;
	}

	private void filterDefMapBuilder() {
		for (Element element : filterDefElement) {
			filterDefMap.put(element.attributeValue("name"), element);
			@SuppressWarnings("unchecked")
			List<Element> filterParamElement = element.elements("filter-param");
			for (Element celement : filterParamElement) {
				duplicateMap.put(celement.attributeValue("name"), celement);
			}
			filterParamMap.put(element.attributeValue("name"), duplicateMap);
		}
	}

	private void SqlQueryMapBuilder() {
		for (Element element : sqlQueryElement) {
			sqlQueryMap.put(element.attributeValue("name"), element);
			@SuppressWarnings("unchecked")
			List<Element> queryParamElement = element.elements("query-param");
			for (Element celement : queryParamElement) {
				duplicateMap2.put(celement.attributeValue("name"), celement);
			}
			sqlQueryParamMap.put(element.attributeValue("name"), duplicateMap2);
		}
	}

	/**
	 * Returns the mapped pojo class name used in hbm file. This method to work
	 * hbm file should have only one class.
	 * 
	 * @return name of pojo class mapped in given hbm file
	 */

	public String getMappedClass() {
		return (classElement.attributeValue("name") == null ? null : classElement.attributeValue("name"));
	}

	/**
	 * 
	 * @return name of table in hbm file
	 */

	public String getMappedTable() {
		if (classElement.attributeValue("table") != null) {
			return classElement.attributeValue("table");
		}
		return getMappedClass().substring(getMappedClass().lastIndexOf(".") + 1);
	}

	/**
	 * @return pojo class property name for id
	 */
	public String getNameOfId() {
		return idElement.attributeValue("name") == null ? null : idElement.attributeValue("name");
	}

	/**
	 * returns the column name of ID
	 * 
	 * @return
	 */
	public String getNameOfIdColumn() {
		if (idElement.attributeValue("column") != null) {
			return idElement.attributeValue("column");
		}
		return getNameOfId();
	}

	/**
	 * @return returns the name of generator used for primary key generation
	 */
	public String getNameOfGenerator() {
		Element generatorElement = idElement.element("generator");
		if (generatorElement == null) {
			return "assigned";
		}
		return generatorElement.attributeValue("class");
	}

	/**
	 * index starts from zero. If index is not in range returns null.
	 * 
	 * @param propertyIndex
	 * @return returns the column name of give property.
	 */
	public String getPropertyColumnName(int propertyIndex) {
		if (propertyIndex >= 0 && propertyElements.size() > propertyIndex) {
			return propertyElements.get(propertyIndex).attributeValue("column");
		}
		return null;
	}

	private boolean checkPropertyElementsIndexRange(int propertyIndex) {
		return (propertyElements.size() > propertyIndex && propertyIndex >= 0);
	}

	/**
	 * If update attribute not used for given property it returns null.
	 * 
	 * @param propertyIndex
	 * @return returns the update attribute value for given property.
	 */
	public String getUpadteAttributeValue(int propertyIndex) {
		boolean flag = checkPropertyElementsIndexRange(propertyIndex);
		if (flag) {
			return propertyElements.get(propertyIndex).attributeValue("update");
		}
		return null;
	}

	/**
	 * pass the index positions of property and pass attribute name. If index is
	 * out of range or given attribute never exist returns null.
	 * 
	 * @param propertyIndex
	 * @param attributeName
	 * @return It returns back value of given attribute for given property.
	 */
	public String getPropertyAttribute(int propertyIndex, String attributeName) {
		boolean flag = checkPropertyElementsIndexRange(propertyIndex);
		if (flag) {
			return propertyElements.get(propertyIndex).attributeValue(attributeName);
		}
		return null;
	}

	/**
	 * This method is used for Set. pass index position of set and name of
	 * attribute. If index is out of range or given attribute never exist
	 * returns null.
	 * 
	 * @param setIndex
	 * @param attributeName
	 * @return It returns back value of given attribute for given set.
	 */
	public String getAttributeOfSet(int setIndex, CollectionAttributes attribute) {
		boolean flag = setIndex >= 0 && setElement.size() > setIndex;
		if (flag) {
			return setElement.get(setIndex).attributeValue(attribute.getName());
		}
		return null;
	}

	/**
	 * This method is used for List. pass index position of set and name of
	 * attribute. It returns back value of given attribute for given list.
	 * 
	 * @param listIndex
	 * @param attributeName
	 * @return
	 */
	public String getAttributeOfList(int listIndex, CollectionAttributes attributeName) {
		boolean flag = listIndex >= 0 && listElement.size() > listIndex;
		if (flag) {
			return setElement.get(listIndex).attributeValue(attributeName.getName());
		}
		return null;
	}

	/**
	 * This method is used for Map. pass index position of map and name of
	 * attribute. It returns back value of given attribute for given Map.
	 * 
	 * @param mapIndex
	 * @param attributeName
	 * @return
	 */
	public String getAttributeOfMap(int mapIndex, CollectionAttributes attributeName) {
		boolean flag = mapIndex >= 0 && mapElement.size() > mapIndex;
		if (flag) {
			return mapElement.get(mapIndex).attributeValue(attributeName.getName());
		}
		return null;
	}

	/**
	 * This method is used for Bag. pass index position of bag and name of
	 * attribute. It returns back value of given attribute for given Bag.
	 * 
	 * @param bagIndex
	 * @param attributeName
	 * @return
	 */
	public String getAttributeOfBag(int bagIndex, CollectionAttributes attributeName) {
		boolean flag = bagIndex >= 0 && bagElement.size() > bagIndex;
		if (flag) {
			return bagElement.get(bagIndex).attributeValue(attributeName.getName());
		}
		return null;
	}

	/**
	 * This method is used for Idbag. pass index position of idbag and name of
	 * attribute. It returns back value of given attribute for given idbag.
	 * 
	 * @param idBagIndex
	 * @param attributeName
	 * @return
	 */
	public String getAttributeOfIdBag(int idBagIndex, CollectionAttributes attributeName) {
		boolean flag = idBagIndex >= 0 && idBagElement.size() > idBagIndex;
		if (flag) {
			return idBagElement.get(idBagIndex).attributeValue(attributeName.getName());
		}
		return null;
	}

	/**
	 * Returns the key column name defined inside set.
	 * 
	 * @param setIndex
	 * @return
	 */
	public String getSetKeyColumn(int setIndex) {
		boolean flag = setIndex >= 0 && setElement.size() > setIndex;
		if (flag) {
			return setElement.get(setIndex).element("key").attributeValue("column");
		}
		return null;
	}

	public String getListKeyColumn(int listIndex) {
		boolean flag = listIndex >= 0 && listElement.size() > listIndex;
		if (flag) {
			return listElement.get(listIndex).element("key").attributeValue("column");
		}
		return null;
	}

	/**
	 * 
	 * @return number of property tags defined in hbm file.
	 */
	public int getPropertiesCount() {
		return propertyElements.size();
	}

	/**
	 * Pass the property name defined in hbm file and pass the attribute to
	 * retrieve value.
	 * 
	 * @param propertyName
	 * @param attribute
	 * @return
	 */
	public String getPropertyAttributeValue(String propertyName, PropertyAttributes attribute) {

		return propertyMap.get(propertyName).attributeValue(attribute.getEname());
	}

	/**
	 * 
	 * @return return list of property names
	 */
	public List<String> getPropertyNames() {
		List<String> listOfProperyNames = new ArrayList<String>();
		for (Element e : propertyElements) {
			listOfProperyNames.add(e.attributeValue("name"));
		}
		return listOfProperyNames;
	}

	/**
	 * 
	 * @return set names defined in hbm file
	 */
	public List<String> getSetNames() {
		List<String> listOfSetNames = new ArrayList<String>();
		for (Element e : setElement) {
			listOfSetNames.add(e.attributeValue("name"));
		}
		return listOfSetNames;
	}

	/**
	 * 
	 * @return list names defined in hbm file
	 */
	public List<String> getListNames() {
		List<String> listNames = new ArrayList<String>();
		for (Element e : listElement) {
			listNames.add(e.attributeValue("name"));
		}
		return listNames;
	}

	/**
	 * 
	 * @return bag names defined in hbm file
	 */
	public List<String> getBagNames() {
		List<String> listOfBagNames = new ArrayList<String>();
		for (Element e : bagElement) {
			listOfBagNames.add(e.attributeValue("name"));
		}
		return listOfBagNames;
	}

	/**
	 * 
	 * @return idbag names defined in hbm file
	 */
	public List<String> getIdBagNames() {
		List<String> listOfIdBagNames = new ArrayList<String>();
		for (Element e : idBagElement) {
			listOfIdBagNames.add(e.attributeValue("name"));
		}
		return listOfIdBagNames;
	}

	/**
	 * 
	 * @return names defined in manytoone tags.
	 */
	public List<String> getManytoOneNames() {
		List<String> mtoNames = new ArrayList<String>();
		for (Element e : manyToOneElement) {
			mtoNames.add(e.attributeValue("name"));
		}
		return mtoNames;
	}

	/**
	 * pass the name of many to one and attribute value which we want to get.
	 * 
	 * @param name
	 * @param attribute
	 * @return value of given attribute for given property
	 */
	public String getManyToOneAttribute(String name, ManyToOneAttributes attribute) {
		return manyToOneMap.get(name).attributeValue(attribute.getName());
	}
	public String getOneToOneProperty(String propertyName,OneToOneAttributes attributeName)
	{
		return oneToOneMap.get(propertyName).attributeValue(attributeName.getoName());
	}

	/**
	 * pass class attribute
	 * 
	 * @param attribute
	 * @return value of give attribute
	 */
	public String getClassAttribute(ClassAttributes attribute) {
		return classElement.attributeValue(attribute.getString());
	}
	/**
	 * pass unionClassName, class level attribute
	 * 
	 * @param className
	 * @param attribuute
	 * @return attribute value for given class.
	 */
	public String getUnionClassAttribute(String className, String attribuute) {
		return unionClassMap.get(className).attributeValue(attribuute);
	}

	/**
	 * pass joinedSubclassName, class level attribute
	 * 
	 * @param className
	 * @param attribute
	 * @return attribute value for given class.
	 */
	public String getJoinedClassAttribute(String className, String attribute) {
		return joinedClassMap.get(className).attributeValue(attribute);
	}

	/**
	 * pass subClass name, class level attribute
	 * 
	 * @param className
	 * @param attribute
	 * @return attribute value for given class
	 */
	public String getSubClassAttriubte(String className, String attribute) {
		return subClassMap.get(className).attributeValue(attribute);
	}

	/**
	 * pass Unionclass index position(index position start from zero)
	 * 
	 * @param index
	 * @return name of class configured in unionsubclass
	 */
	public String getUnionClassName(int index) {
		return unionSubClass.get(index).attributeValue("name");
	}

	/**
	 * pass SubClass index position. (note: this method don't work if you define
	 * subclass inside a class
	 * 
	 * @param index
	 * @return name of class configured in subclass
	 */
	public String getSubClassName(int index) {
		return subClass.get(index).attributeValue("name");
	}

	/**
	 * pass subclass index position
	 * 
	 * @param index
	 * @return name of class configured in joinedSubClass
	 */
	public String getJoinedSubClassName(int index) {
		return joinedSubClass.get(index).attributeValue("name");
	}

	/**
	 * Pass name of filter
	 * 
	 * @param filterName
	 * @return condition mentioned in filter
	 */
	public String getFilterCondition(String filterName) {
		return filterMap.get(filterName).attributeValue("condition");
	}

	/**
	 * 
	 * @return number of filters defined in hbm file.
	 */
	public int getNumberOfFilters() {
		return filterElement.size();
	}

	/**
	 * 
	 * @return number of filters-def defined in hbm file.
	 */
	public int getNumberOfFilterDefinations() {
		return filterDefElement.size();
	}

	/**
	 * pass name of FilterDefination
	 * 
	 * @param filterDefinaionName
	 * @return condition mentioned in filter-def
	 */
	public String getFilterDefCondition(String filterDefinaionName) {
		return filterDefMap.get(filterDefinaionName).attributeValue("condition");
	}

	public String getParamNameInFilterDef(String filterName, String paramName) {
		return filterParamMap.get(filterName).get(paramName).attributeValue("name");
	}

	public String getParamTypeInFilterDef(String filterName, String paramName) {
		return filterParamMap.get(filterName).get(paramName).attributeValue("type");
	}

	public List<String> getParamNamesInFilter(String filterName) {
		@SuppressWarnings("unchecked")
		List<String> name = (List<String>) filterDefMap.get(filterName).elements("filter-param");
		if (name.size() == 0) {
			return null;
		}
		return name;
	}

	public int getNumberOfFilterParams(String filterName) {
		return filterDefMap.get(filterName).elements("filter-param").size();
	}

	public int getNumberOfQuires() {
		return queryElement.size();
	}

	public String getQuery(String queryName) {
		return queryMap.get(queryName).getStringValue().trim();
	}
	
	public String getSQLQuery(String name)
	{
		return sqlQueryMap.get(name).getStringValue().trim();
	}
	
	public int getNumberOfSQLQuires()
	{
		return sqlQueryElement.size();
	}
	
	public String getSQLQueryAttribute(String queryName,String elementName,String attributeName)
	{
		return sqlQueryMap.get(queryName).element(elementName).attributeValue(attributeName);
	}
	public String getQueryAttribute(String queryName,String elementName,String attributeName)
	{
		return queryMap.get(queryName).element(elementName).attributeValue(attributeName);
	}
	
	public String getOneToManyAttribute(CollectionType type,int index, OneToManyAttributes attributeName)
	{
		return getMappingRelationValue(type, index, "one-to-many", attributeName.getName());
	}
	
	public String getManyToManyAttribute(CollectionType type,int index, ManyToManyAttributes attributeName)
	{
		return getMappingRelationValue(type, index, "many-to-many", attributeName.getName());
	}
	
	private String getMappingRelationValue(CollectionType type,int index, String relation,String attributeName)
	{
		String attributeValue = null;
		if(type.toString().equals("set"))
		{
			attributeValue=setElement.get(index).element(relation).attributeValue(attributeName);
		}
		else if(type.equals("list"))
		{
			attributeValue=listElement.get(index).element(relation).attributeValue(attributeName);
		}
		else if(type.equals("map"))
		{
			attributeValue=mapElement.get(index).element(relation).attributeValue(attributeName);
		}
		return attributeValue;
	}
	
}
