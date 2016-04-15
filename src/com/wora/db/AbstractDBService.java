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
import java.util.Properties;

import org.apache.log4j.Logger;

public abstract class AbstractDBService {
	protected static Logger logger = Logger.getLogger(AbstractDBService.class);

	protected Connection dbConnection = null;
	protected String jdbcURL = "jdbc:hsqldb:file:tbpapidb";
	protected String dbUserName = "sa";
	protected String dbPassword = "";
	protected String dbValidationQuery = "";
	protected String dbDriver = "org.hsqldb.jdbcDriver";

	protected static Map<String, PreparedStatement> statementMap = Collections.synchronizedMap(new HashMap<String, PreparedStatement>());

	public abstract void checkDbMetaData();

	public abstract void maintainTables();

	// Initialize the data fields
	public void init(Properties appProps) throws Exception {

		logger.debug("Db initalizing..");

		jdbcURL = appProps.getProperty("database.jdbc.url", "").trim();
		dbUserName = appProps.getProperty("database.username", "").trim();
		dbPassword = appProps.getProperty("database.password", "").trim();
		dbValidationQuery = appProps.getProperty("database.validation.query", "").trim();
		dbDriver = appProps.getProperty("database.jdbc.driver", "").trim();

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

}
