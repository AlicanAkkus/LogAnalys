package com.wora.db;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Locale;

import com.wora.bean.ContextDestroyLineBean;
import com.wora.bean.ContextInitializeLineBean;
import com.wora.bean.MethodLineBean;
import com.wora.bean.VariableLineBean;

public class DerbyDAOImp extends AbstractDBService {

	public void insertMethodLog(MethodLineBean bean) {

		logger.debug("inserting Method Line Log is started.");
		logger.debug(bean.toString());

		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			StringBuilder query = new StringBuilder("INSERT INTO METHOD_ANALYS(METHOD_NAME, EXECUTE_TIME, DATE, ANALYS_TYPE) VALUES(?, ?, ?, ?)");

			checkDbConnection();
			statement = statementMap.get(query);
			if (statement == null) {
				logger.debug("Creating statement method line bean : " + query);
				statement = dbConnection.prepareStatement(query.toString());
				statementMap.put(query.toString(), statement);
			}

			statement.setString(1, bean.getMethodName());
			statement.setInt(2, bean.getExecuteTime());
			statement.setTimestamp(3, new Timestamp(bean.getDate().getTime()));
			statement.setString(4, bean.getAnalysType());

			statement.execute();

		} catch (Exception e) {
			logger.error(e, e);
			statementMap.remove(statement);
		} finally {
			closeResultSet(rs);
			closeStatement(statement);
		}

		logger.debug("inserting Method Line Log is finished.");
	}

	public void insertVariableMethodLog(VariableLineBean bean) {

		logger.debug("inserting Variable Line Log is started.");
		logger.debug(bean.toString());

		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			StringBuilder query = new StringBuilder("INSERT INTO VARIABLE_ANALYS(METHOD_NAME, SUM_MEMORY, DATE, ANALYS_TYPE) VALUES(?, ?, ?, ?)");

			checkDbConnection();
			statement = statementMap.get(query);
			if (statement == null) {
				logger.debug("Creating statement variable line bean : " + query);
				statement = dbConnection.prepareStatement(query.toString());
				statementMap.put(query.toString(), statement);
			}

			statement.setString(1, bean.getMethodName());
			statement.setInt(2, bean.getSumMemory());
			statement.setTimestamp(3, new Timestamp(bean.getDate().getTime()));
			statement.setString(4, bean.getAnalysType());

			statement.execute();

		} catch (Exception e) {
			logger.error(e, e);
			statementMap.remove(statement);
		} finally {
			closeResultSet(rs);
			closeStatement(statement);
		}

		logger.debug("inserting Variable Line Log is finished.");
	}

	public void insertContextInitializeLog(ContextInitializeLineBean bean) {

		logger.debug("inserting Context initialize Line Log is started.");
		logger.debug(bean.toString());

		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			StringBuilder query = new StringBuilder(
					"INSERT INTO CONTEXT_INITIALIZE_ANALYS(METHOD_NAME, DESCRIPTION, EXECUTE_TIME, DATE, ANALYS_TYPE) VALUES(?, ?, ?, ?, ?)");

			checkDbConnection();
			statement = statementMap.get(query);
			if (statement == null) {
				logger.debug("Creating statement context initialize line bean : " + query);
				statement = dbConnection.prepareStatement(query.toString());
				statementMap.put(query.toString(), statement);
			}

			statement.setString(1, bean.getContextInializeMethod());
			statement.setString(2, bean.getDescription());
			statement.setInt(3, bean.getExecuteTime());
			statement.setTimestamp(4, new Timestamp(bean.getDate().getTime()));
			statement.setString(5, bean.getAnalysType());

			statement.execute();

		} catch (Exception e) {
			logger.error(e, e);
			statementMap.remove(statement);
		} finally {
			closeResultSet(rs);
			closeStatement(statement);
		}

		logger.debug("inserting Context initialize Line Log is finished.");
	}

	public void insertContextDestroyLog(ContextDestroyLineBean bean) {

		logger.debug("inserting Context destroy Line Log is started.");
		logger.debug(bean.toString());

		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			StringBuilder query = new StringBuilder(
					"INSERT INTO CONTEXT_DESTROY_ANALYS(METHOD_NAME, DESCRIPTION, EXECUTE_TIME, DATE, ANALYS_TYPE) VALUES(?, ? ,?, ?, ?)");

			checkDbConnection();
			statement = statementMap.get(query);
			if (statement == null) {
				logger.debug("Creating statement context destroy line bean : " + query);
				statement = dbConnection.prepareStatement(query.toString());
				statementMap.put(query.toString(), statement);
			}

			statement.setString(1, bean.getContexyDestroyedMethod());
			statement.setString(2, bean.getDescription());
			statement.setInt(3, bean.getExecuteTime());
			statement.setTimestamp(4, new Timestamp(bean.getDate().getTime()));
			statement.setString(5, bean.getAnalysType());

			statement.execute();

		} catch (Exception e) {
			logger.error(e, e);
			statementMap.remove(statement);
		} finally {
			closeResultSet(rs);
			closeStatement(statement);
		}

		logger.debug("inserting Context destroy Line Log is finished.");
	}

	@Override
	public void checkDbMetaData() {

		logger.debug("Checking metadata...");
		DatabaseMetaData dbMetaData = null;
		try {
			checkDbConnection();
			dbMetaData = dbConnection.getMetaData();
			// Log Table
			if (!tableExists(dbMetaData, "METHOD_ANALYS")) {
				logger.debug("METHOD_ANALYS does not exist. Creating table : METHOD_ANALYS");
				createMethodAnalysTable();
			}

			if (!tableExists(dbMetaData, "VARIABLE_ANALYS")) {
				logger.debug("METHOD_ANALYS does not exist. Creating table : VARIABLE_ANALYS");
				createVariableAnalysTable();
			}

			if (!tableExists(dbMetaData, "CONTEXT_INITIALIZE_ANALYS")) {
				logger.debug("METHOD_ANALYS does not exist. Creating table : CONTEXT_INITIALIZE_ANALYS");
				createContextInitializeAnalysTable();
			}

			if (!tableExists(dbMetaData, "CONTEXT_DESTROY_ANALYS")) {
				logger.debug("METHOD_ANALYS does not exist. Creating table : CONTEXT_INITIALIZE_ANALYS");
				createContextDestroyeAnalysTable();
			}

			cleanUpDatabase();
			maintainTables();

		} catch (java.sql.SQLException e) {
			logger.error(e, e);
		}
	}

	private void createMethodAnalysTable() {

		logger.info("Creating METHOD_ANALYS table....");
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			StringBuffer sql = new StringBuffer("CREATE TABLE METHOD_ANALYS(");
			sql.append(" ID int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , ");
			sql.append(" METHOD_NAME varchar(200), ");
			sql.append(" EXECUTE_TIME int, ");
			sql.append(" ANALYS_TYPE varchar(10), ");
			sql.append(" DATE timestamp)");
			stmt.execute(sql.toString());

		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception exc) {
				//
			}
		}
	}

	private void createVariableAnalysTable() {

		logger.info("Creating VARIABLE_ANALYS Table....");
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			StringBuffer sql = new StringBuffer("CREATE TABLE VARIABLE_ANALYS(");
			sql.append(" ID int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , ");
			sql.append(" METHOD_NAME VARCHAR(200), ");
			sql.append(" SUM_MEMORY int, ");
			sql.append(" ANALYS_TYPE varchar(10), ");
			sql.append(" DATE timestamp)");
			stmt.execute(sql.toString());
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception exc) {
				//
			}
		}
	}

	private void createContextInitializeAnalysTable() {

		logger.info("Creating CONTEXT_INITIALIZE_ANALYS Table....");
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			StringBuffer sql = new StringBuffer("CREATE TABLE CONTEXT_INITIALIZE_ANALYS(");
			sql.append(" ID int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , ");
			sql.append(" METHOD_NAME VARCHAR(200), ");
			sql.append(" DESCRIPTION VARCHAR(200), ");
			sql.append(" EXECUTE_TIME int, ");
			sql.append(" ANALYS_TYPE varchar(10), ");
			sql.append(" DATE timestamp) ");

			stmt.execute(sql.toString());
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception exc) {
				//
			}
		}
	}

	private void createContextDestroyeAnalysTable() {

		logger.info("Creating CONTEXT_DESTROY_ANALYS Table....");
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			StringBuffer sql = new StringBuffer("CREATE TABLE CONTEXT_DESTROY_ANALYS(");
			sql.append(" ID int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , ");
			sql.append(" METHOD_NAME VARCHAR(200), ");
			sql.append(" DESCRIPTION VARCHAR(200), ");
			sql.append(" EXECUTE_TIME int, ");
			sql.append(" ANALYS_TYPE varchar(10), ");
			sql.append(" DATE timestamp) ");

			stmt.execute(sql.toString());
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception exc) {
				//
			}
		}
	}

	private void compressTable(String tableName) {
		try {
			logger.info("Maintaining table : " + tableName);
			CallableStatement cs = dbConnection.prepareCall("CALL SYSCS_UTIL.SYSCS_COMPRESS_TABLE(?, ?, ?)");
			cs.setString(1, dbUserName.toUpperCase());
			cs.setString(2, tableName);
			cs.setShort(3, (short) 1);
			cs.execute();
		} catch (SQLException e) {
			logger.error(e, e);
		}
	}

	@Override
	public void maintainTables() {
		DatabaseMetaData dbMetaData = null;
		try {
			checkDbConnection();
			dbMetaData = dbConnection.getMetaData();
			String[] types = { "TABLE" };
			ResultSet rs = dbMetaData.getTables(null, null, "%", types);
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				compressTable(tableName);
			}
		} catch (SQLException e) {
			logger.error(e, e);
		}
	}

	// ///////METADATA
	public boolean tableExists(DatabaseMetaData dbMetaData, String tableName) throws SQLException {

		return tableExistsCaseSensitive(dbMetaData, tableName) || tableExistsCaseSensitive(dbMetaData, tableName.toUpperCase(Locale.US))
				|| tableExistsCaseSensitive(dbMetaData, tableName.toLowerCase(Locale.US));
	}

	public boolean tableExistsCaseSensitive(DatabaseMetaData dbMetaData, String tableName) throws SQLException {

		ResultSet rsTables = dbMetaData.getTables(null, null, tableName, null);
		try {
			boolean found = rsTables.next();
			return found;
		} finally {
			closeResultSet(rsTables);
		}
	}

}
