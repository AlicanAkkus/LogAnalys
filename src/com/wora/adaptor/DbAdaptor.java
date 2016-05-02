package com.wora.adaptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DbAdaptor extends AbstractAdaptor {

	private Logger logger = Logger.getLogger(DbAdaptor.class);
	private String DB_URL, DB_DRIVER, DB_USER, DB_PASS = null;
	private int DELAY = 1000;

	@Override
	public void init(Element destination) {
		logger.info("DbAdaptor is initializing..");

		try {
			NodeList params = XPathAPI.selectNodeList(destination, "param");
			for (int i = 0; i < params.getLength(); i++) {
				Element param = (Element) params.item(i);

				String name = param.getAttribute("name");
				String value = param.getAttribute("value");
				if ("dbDriver".equalsIgnoreCase(name)) {
					DB_DRIVER = value;
				} else if ("dbUrl".equalsIgnoreCase(name)) {
					DB_URL = value;
				} else if ("dbUser".equalsIgnoreCase(name)) {
					DB_USER = value;
				} else if ("dbPassword".equalsIgnoreCase(name)) {
					DB_PASS = value;
				} else if ("delay".equalsIgnoreCase(name)) {
					DELAY = Integer.valueOf(value);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			try {
				Class.forName(DB_DRIVER);
			} catch (ClassNotFoundException e) {
				logger.error(e, e);
			}
		}
		logger.info("DbAdaptor is initialized..");
	}

	@Override
	public void processMessage(Object message) {
		logger.info("Message saved to database : " + message);

		
		Statement statement = null;
		Connection connection = null;
		try{
			Thread.sleep(DELAY);
			
			connection = getConnection();
			
			statement = connection.createStatement();
			statement.execute(message.toString());
		}catch(Exception e){
			logger.error(e, e);
		}finally{
			closeStatement(statement);
			closeConnection(connection);
		}
		
	}
	
	public Connection getConnection() throws Exception{
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
	}
	
	public void closeStatement(Statement statement){
		try{
			if(statement != null)
				statement.close();
		}catch(Exception e){
			logger.error(e, e);
		}
	}
	
	public void closeConnection(Connection connection){
		try{
			if(connection != null)
				connection.close();
		}catch(Exception e){
			logger.error(e, e);
		}
	}

}
