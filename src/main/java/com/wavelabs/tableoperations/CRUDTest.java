package com.wavelabs.tableoperations;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

/**
 * this class will tell you about Table informations
 * 
 * @author tharunkumarb
 */

public class CRUDTest {


	private Session session = null;
	private SessionFactory factory = null;
	private PersistentClass pc = null;
	private Table table = null;
	private Configuration config = null;

	public CRUDTest(SessionFactory sf, Configuration cfg, Session sesion) {
		factory = sf;
		config = cfg;
		session = sesion;
	}

	public void setSession(Session session) {
		this.session = session;
	}


	/**
	 * this method will get the persistentClass reference
	 * 
	 * @param entityName
	 * @return persistentClass
	 */
	public PersistentClass getPersistentClass(String entityName) {
		pc = config.getClassMapping(entityName);
		return pc;
	}

	private void intillization(String entityName) {
		pc = config.getClassMapping(entityName);
		table = pc.getTable();
	}

	/**
	 * this method will return the org.hibernate.mapping.Table reference
	 * 
	 * @param entityName
	 * @return Table
	 */
	public Table getTable(String pojoclassName) {
		PersistentClass pc = getPersistentClass(pojoclassName);
		Table table = pc.getTable();
		return table;
	}

	/**
	 * it will get the table name from Table interface
	 * 
	 * @param pojoclassName
	 * @return
	 */

	public String getTableName(String pojoclassName) {
		table = getTable(pojoclassName);
		return table.getName();
	}

	/**
	 * this method will checks the table is created in Database or not if the
	 * table is there it returns true otherwise false
	 * 
	 * @param entityName
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	public boolean isTableCreated(String entityName) {
		String tableName = null;
		try {
			intillization(entityName);
			tableName = table.getName();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		SessionFactoryImplementor sessionFactoryImplementor = null;
		java.sql.Connection connection = null;
		try {
			sessionFactoryImplementor = (SessionFactoryImplementor) factory;
			connection = sessionFactoryImplementor.getConnectionProvider().getConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean tExists = false;
		try {
			ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
			while (rs.next()) {
				String tName = rs.getString("TABLE_NAME");
				if (tName != null && tName.equals(tableName)) {
					tExists = true;
					break;
				}
				connection.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return tExists;
	}

	/**
	 * this method will check whether the record id updated or not if the record
	 * id updated it returns true otherwise false to use this method we should
	 * follow 1. the primary key column should be the first column in the table
	 * 2. the relation columns(i.e foreign key) will not be checked
	 * 
	 * @param className
	 * @param propertyName
	 * @param updatedValue
	 * @param primaryKey
	 * @return boolean
	 */
	public boolean isColumnUpdated(Class<?> className, String propertyName, Object updatedValue, int primaryKey) {
		intillization(className.getName());
		return  (updatedValue.equals(getValue(className, primaryKey, propertyName))) ? true : false;
	}

	/**
	 * checking column updations using entityname
	 * 
	 * @param entityName
	 * @param propertyName
	 * @param updatedValue
	 * @param primaryKey
	 * @return boolean
	 */

	public boolean isColumnUpdated(String entityName, String propertyName, Object updatedValue, int primaryKey) {

		intillization(entityName);
		return  (updatedValue==getValue(entityName, primaryKey, propertyName)) ? true : false;

	}

	/**
	 * 
	 * this method will check whether the record is deleted or not is that
	 * record is deleted it returns true otherwise false
	 * 
	 * @param className
	 * @param primaryKey
	 * @return boolean
	 */

	public boolean isRecordDeleted(Class<?> className, int primaryKey) {
		
		return !isRecordExist(getTableName(className.getName()), primaryKey);
	}

	/**
	 * this will checks the records deletions using entityName
	 * 
	 * @param entityName
	 * @param primaryKey
	 * @return boolean
	 */
	public boolean isRecordDeleted(String entityName, int primaryKey) {
		return !isRecordExist(getTableName(entityName), primaryKey);
	}

	/**
	 * this method will checks number of records deleted or not the result is
	 * created in the form of boolean[].
	 * 
	 * @param className
	 * @param primaryKeys
	 * @return boolean
	 */
	public boolean[] isRecordsDeleted(Class<?> className, int[] primaryKeys) {
		boolean[] result = new boolean[primaryKeys.length];
		for (int i = 0; i < primaryKeys.length; i++) {
			result[i] = isRecordDeleted(className, primaryKeys[i]);
		}
		return result;
	}

	/**
	 * this will checks the records deletion using entityname
	 * 
	 * @param entityName
	 * @param primaryKeys
	 * @return
	 */
	public boolean[] isRecordsDeleted(String entityName, int[] primaryKeys) {
		boolean[] result = new boolean[primaryKeys.length];
		for (int i = 0; i < primaryKeys.length; i++) {
			result[i] = isRecordDeleted(entityName, primaryKeys[i]);
		}
		return result;
	}

	/**
	 * this method will check whether records are inserted or not if the records
	 * are inserted then it returns true otherwise false
	 * 
	 * @param className
	 * @param primaryKey
	 * @return boolean
	 */
	public boolean isRecordInserted(Class<?> className, int primaryKey) {
		return isRecordExist(getTableName(className.getName()), primaryKey);
	}
	private boolean isRecordExist(String tableName,int pk)
	{
		String sql = "select * from " + tableName + " as e where id=:pk";
		Object obj = session.createSQLQuery(sql).setParameter("pk", pk).uniqueResult();
		return (obj==null) ? false : true;
	}

	/**
	 * this will checks the records insertions using entityname
	 * 
	 * @param entityName
	 * @param primaryKey
	 * @return boolean
	 */

	public boolean isRecordInserted(String entityName, int primaryKey) {
		return isRecordExist(getTableName(entityName), primaryKey);
	}

	/**
	 * this will check the number of records inserted or not it returns the
	 * boolean array which contains result of inserted records.
	 * 
	 * @param className
	 * @param primaryKeys
	 * @return boolean
	 */

	public boolean[] isRecordsInserted(Class<?> className, int[] primaryKeys) {
		boolean[] result = new boolean[primaryKeys.length];
		for (int i = 0; i < primaryKeys.length; i++) {
			result[i] = isRecordExist(getTableName(className.getName()), primaryKeys[i]);
		}
		return result;
	}

	/**
	 * this will check the number of records inserted or not it returns the
	 * boolean array which contains result of inserted records.
	 * 
	 * @param entityName
	 * @param primaryKeys
	 * @return boolean
	 */

	public boolean[] isRecordsInserted(String entityName, int[] primaryKeys) {
		boolean[] result = new boolean[primaryKeys.length];
		for (int i = 0; i < primaryKeys.length; i++) {
			result[i] = isRecordExist(getTableName(entityName), primaryKeys[i]);
		}
		return result;
	}


	/**
	 * this method will print all the columns from Table as a String[] format
	 * 
	 * @param entityName
	 * @param totalNoofColumns
	 * @return
	 */
	public String[] getAllColumnNames(Class<?> className, int totalNoofColumns) {
		intillization(className.getName());
		@SuppressWarnings("unchecked")
		Iterator<Column> it = table.getColumnIterator();
		String[] colNames = new String[totalNoofColumns];
		for (int i = 0; i < colNames.length; i++) {
			colNames[i] = it.next().getName();
		}

		return colNames;
	}

	/**
	 * this method will print the column columnName in mapping.
	 * 
	 * @param entityName
	 * @param colNoInOrder
	 * @return String
	 */

	public String getColumnName(Class<?> className, int colNoInOrder) {
		intillization(className.getName());
		return  table.getColumn(colNoInOrder).getName();
	}
	
	/**
	 * it will prints the all property types in the form of Type[] in order of
	 * properties
	 * 
	 * @param className
	 * @param totalNoofColumns
	 * @return Type
	 */
	public Type[] getAllColumnsType(@SuppressWarnings("rawtypes") Class className) {
		ClassMetadata cmd = factory.getClassMetadata(className);
		Type[] type = cmd.getPropertyTypes();
		return type;
	}

	/**
	 * this will give you the type of the property
	 * 
	 * @param className
	 * @param propertyName
	 * @return String
	 */
	public String getColumnType(@SuppressWarnings("rawtypes") Class className, String propertyName) {
		ClassMetadata cmd = factory.getClassMetadata(className);
		return cmd.getPropertyType(propertyName).getName();
	}

	/**
	 * it will give the value at the specified primarykey at specified
	 * propertyName
	 * 
	 * @param className
	 * @param primaryKey
	 * @param propertyName
	 * @return Object
	 */

	public Object getValue(@SuppressWarnings("rawtypes") Class className, int primaryKey, String propertyName) {
		
		return giveValue(className.getName(), propertyName, primaryKey);
	}
	public Object getValue(String entityName, int primaryKey, String propertyName) {

		return giveValue(entityName, propertyName, primaryKey);
	}
	
	private Object giveValue(String className,String propertyName,int primaryKey)
	{
		Table table = getTable(className);
		String pkname = table.getColumn(1).getName();
		Query query = session.createSQLQuery(
				"select " + new Column(propertyName).getName() + " from " + table.getName() + " where " + pkname + "=" + primaryKey);
		return query.uniqueResult();
	}

}