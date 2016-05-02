package com.wora.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.h2.tools.Server;
import org.w3c.dom.Document;

import com.wora.bean.LineBean;
import com.wora.template.HadoopTemplate;

public class H2DaoImpl extends AbstractDBService {
	Server server = null;

	@Override
	public void init(Document document) throws Exception {
		super.init(document);
		server = Server.createWebServer().start();
		logger.debug("H2 memory started at : " + server.getURL());
	}

	@Override
	public void checkDbMetaData(ConcurrentHashMap<String, HadoopTemplate> templatePool) {

		logger.debug("Checking metadata...");
		try {
			checkDbConnection();

			Collection<HadoopTemplate> templates = (Collection<HadoopTemplate>) templatePool.values();

			for (HadoopTemplate template : templates) {
				String tableName = template.getTemplateName();
				LinkedList<LineBean> columns = template.getDataLine();

				StringBuilder createScript = new StringBuilder("create table ").append(tableName).append("(");
				StringBuilder selectScript = new StringBuilder("select * from ").append(tableName);
				createScript.append("ID int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , ");
				for (LineBean column : columns) {
					createScript.append(column.getName()).append(" varchar, ");
				}

				createScript.delete(createScript.lastIndexOf(","), createScript.length());
				createScript.append(" )");
				logger.debug("Create script : " + createScript.toString());

				Statement statement = dbConnection.createStatement();
				statement.execute(createScript.toString());
				statement.close();

				select(selectScript);
			}

			cleanUpDatabase();

		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	private void select(StringBuilder select){
		checkDbConnection();
		
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(select.toString());
			
			if(rs.next()){
				
			}else{
				logger.info("table empty!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				logger.error(exc, exc);
			}
		}
	}

}
