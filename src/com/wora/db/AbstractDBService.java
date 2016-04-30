package com.wora.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wora.template.HadoopTemplate;
import com.wora.util.XmlUtils;

public abstract class AbstractDBService {
	protected static Logger logger = Logger.getLogger(AbstractDBService.class);

	protected Connection dbConnection = null;
	protected String jdbcURL = "jdbc:hsqldb:file:api";
	protected String dbName = "";
	protected String dbUserName = "sa";
	protected String dbPassword = "";
	protected String dbValidationQuery = "";
	protected String dbDriver = "org.hsqldb.jdbcDriver";
	protected String dbMode = "";
	protected int delay = -1;

	protected static Map<String, PreparedStatement> statementMap = Collections.synchronizedMap(new HashMap<String, PreparedStatement>());
	public abstract void checkDbMetaData(ConcurrentHashMap<String, HadoopTemplate> templatePool);

	// Initialize the data fields
	public void init(Document document) throws Exception {
		logger.debug("Db initalizing..");

		Element databaseElement = XmlUtils.findNode(document, "//database");
		NodeList paramElements = XPathAPI.selectNodeList(databaseElement, "param");
		logger.debug("Param length : " + paramElements.getLength());
		
//		//<param name="driver" value="org.h2.Driver" />
//		<param name="baseUrl" value="jdbc:h2:mem" />
//		<param name="dbName" value="bitirme" />
//		<param name="dbUser" value="wora" />
//		<param name="dbPassword" value="wora" />
//		<param name="mode" value="Derby" />
//		<param name="delay" value="-1" />
		for(int i=0; i< paramElements.getLength(); i++){
			Element param = (Element) paramElements.item(i);
			
			String name = param.getAttribute("name");
			String value = param.getAttribute("value");
			
			if("driver".equalsIgnoreCase(name)){
				dbDriver = value;
			}else if("baseUrl".equals(name)){
				jdbcURL = value;
			}else if("dbName".equalsIgnoreCase(name)){
				dbName = value;
			}else if("dbPassword".equalsIgnoreCase(name)){
				dbPassword = value;
			}else if("dbUser".equalsIgnoreCase(name)){
				dbUserName = value;
			}else if("mode".equalsIgnoreCase(name)){
				dbMode = value;
			}else if("delay".equalsIgnoreCase(name)){
				delay = Integer.parseInt(value);
			}
		}
		
		jdbcURL = jdbcURL + ":" + dbName + ";MODE="+dbMode;
		logger.debug(printDBProperties());
		
		checkDbConnection();
		logger.debug("Database object is initialized");
	}

	public synchronized void checkDbConnection() {

		if (dbConnection == null || !validateDbConnection(dbValidationQuery, dbConnection)) {
			getDBConnection();
		}
	}

	protected synchronized void getDBConnection() {

		try {
			logger.debug("get Db Connection ");
			Class.forName(dbDriver).newInstance();
			dbConnection = DriverManager.getConnection(jdbcURL, dbUserName, dbPassword);
			statementMap.clear();
			logger.debug("Connection taken ");
		} catch (Exception e) {
			logger.fatal("Database connection couldn't be established...", e);
		}
	}

	public synchronized boolean validateDbConnection(String dbValidationQuery, Connection dbConnection) {

		logger.debug("DB connection validating....");
		PreparedStatement stmt = null;
		try {
			if (dbConnection != null) {
				stmt = statementMap.get(dbValidationQuery);
				if (stmt == null) {
					logger.debug("Creating statement validateDbConnection : " + dbValidationQuery);
					stmt = dbConnection.prepareStatement(dbValidationQuery);
					statementMap.put(dbValidationQuery, stmt);
				}
				stmt.clearParameters();

				stmt.execute();
				return true;
			}
		} catch (Exception e) {
			closeStatement(stmt);
			statementMap.remove(dbValidationQuery);
			logger.error(e, e);
			dbConnection = null;
		}

		return false;
	}

	public void disconnect() {
		try {
			dbConnection.close();
		} catch (Exception e) {
			logger.error(e, e);
		}
		logger.debug("Database Connection is terminated");
	}

	public static void closeResultSet(ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			logger.error("Cannot close resultset:" + e);
		}
	}

	public static void closeStatement(Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			logger.error("Cannot close statement:" + e);
		}
	}

	public static void closeStatement(PreparedStatement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			logger.error("Cannot close prepared statement:" + e);
		}
	}

	public void cleanUpDatabase() {

		logger.debug("DBService - cleanUpDatabase begin..");

		logger.debug("cleanUpDatabase end..");
	}

	public void setAutoCommit(boolean status) {
		try {
			dbConnection.setAutoCommit(status);
		} catch (SQLException e) {
			logger.error(e, e);
		}
	}

	public void commit() {

		try {
			dbConnection.commit();
		} catch (SQLException e) {
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				logger.error(e1, e1);
			}
		} finally {
			try {
				dbConnection.setAutoCommit(true);
			} catch (SQLException e) {
				logger.error(e, e);
			}
		}
	}

	public void rollBack() {
		try {
			dbConnection.rollback();
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	public String printDBProperties() {
		return "AbstractDBService [dbConnection=" + dbConnection + ", jdbcURL=" + jdbcURL + ", dbName=" + dbName + ", dbUserName=" + dbUserName
				+ ", dbPassword=" + dbPassword + ", dbValidationQuery=" + dbValidationQuery + ", dbDriver=" + dbDriver + ", dbMode=" + dbMode + ", delay="
				+ delay + "]";
	}

		
}