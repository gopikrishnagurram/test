package com.wavelabs.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;
import org.hibernate.SessionFactory;
import org.hibernate.internal.util.xml.XmlDocument;

/**
 * Returns all the meta data of given configuration file
 * 
 * @author gopikrishnag
 *
 */
public class CfgFileMetaData {

	private XmlDocument xmd = null;
	private Element sessionFactoryElement = null;
	private List<Element> propertyElements = null;
	private Map<String, Element> mapOfProperties = new HashMap<String, Element>();
	private List<Element> resources = null;

	@SuppressWarnings("unchecked")
	public CfgFileMetaData(XmlDocument xm, SessionFactory factory) {
		isSessionFactoryBuilt(factory);
		this.xmd = xm;
		sessionFactoryElement = xmd.getDocumentTree().getRootElement().element("session-factory");
		propertyElements = sessionFactoryElement.elements("property");
		resources = sessionFactoryElement.elements("mapping");
		buildPropertyMapping();
	}

	private void isSessionFactoryBuilt(SessionFactory factory) {
		if (factory instanceof SessionFactory) {
			System.out.println("Schema exported completed");
		}

		else {
			throw new NullPointerException("SessionFactory object not created");
		}
	}

	private void buildPropertyMapping() {
		for (Element element : propertyElements) {
			mapOfProperties.put(element.attributeValue("name").trim(), element);
		}
	}

	/**
	 * 
	 * @return hbm2auto.ddl value mentioned in cfg file. If not configured
	 *         returns null.
	 */
	public String gethbm2AutoDdl() {
		return mapOfProperties.get("hbm2auto.ddl").getStringValue().trim();
	}

	/**
	 * 
	 * @return returns the show_sql property value
	 */
	public String getShowSql() {
		return mapOfProperties.get("show_sql").getStringValue().trim();
	}

	/**
	 * 
	 * @return returns the format_sql value
	 */
	public String getFormatSql() {
		return mapOfProperties.get("format_sql").getStringValue().trim();
	}

	/**
	 * 
	 * @return returns the sql_comment_value
	 */
	public String getShowSqlComments() {
		return mapOfProperties.get("use_sql_comments").getStringValue().trim();
	}

	/**
	 * Return the dialect used in cfg file
	 * 
	 * @return
	 */
	public String getDialect() {
		return mapOfProperties.get("dialect").getStringValue().trim();
	}

	/**
	 * 
	 * @return password mentioned in cfg file
	 */
	public String getPassword() {
		return mapOfProperties.get("connection.password").getStringValue().trim();
	}

	/**
	 * 
	 * @return username mentioned in cfg file
	 */
	public String getUserName() {
		return mapOfProperties.get("connection.username").getStringValue().trim();
	}

	/**
	 * to get correct result, give property name as connection.url in cfg file
	 * 
	 * @return name of url given in cfg file
	 */
	public String getConnectionUrl() {
		return mapOfProperties.get("connection.url").getStringValue().trim();
	}

	/**
	 * use property name as connection.driver_class as name.
	 * 
	 * @return name of driver_class used in cfg file.
	 */
	public String getDriverName() {
		return mapOfProperties.get("connection.driver_class").getStringValue().trim();
	}

	/**
	 * 
	 * @return List of resource names configured in cfg file.
	 */
	public List<String> getResourceNames() {
		List<String> listOfNames = new ArrayList<String>();
		for (Element element : resources) {
			listOfNames.add(element.attributeValue("resource").trim());
		}
		return listOfNames;
	}

	/**
	 * pass name of property as parameter. If parameter not exist in cfg file,
	 * it will return null
	 * 
	 * @param propertyName
	 * @return value of given property
	 */
	public String getProperyValue(String propertyName) {
		return mapOfProperties.get(propertyName).getStringValue();
	}

	/**
	 * Based on given index position returns the name of mapping resource
	 * 
	 * @param index
	 * @return
	 */
	public String getResourceName(int index) {
		return resources.get(index).attributeValue("resource").trim();
	}
}
