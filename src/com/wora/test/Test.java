package com.wora.test;

import java.text.SimpleDateFormat;

import com.wora.constant.IOConstant;

public class Test implements IOConstant {

	public static void main(String[] args) throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");

		System.out.println(dateFormat.parse("05 Jan 2016 10:57:21").toString());

		// String fileName = "LogAnalys.properties";
		// Properties properties = new Properties();
		// properties.load(new FileInputStream(new File(fileName)));
		//
		//
		// Tomcat7 tomcat = new Tomcat7();
		// tomcat.initialize(properties);
		// tomcat.startTomcat();
		//
		//
		// while(true){
		//
		// }

	}
}
