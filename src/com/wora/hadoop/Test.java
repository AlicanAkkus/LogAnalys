package com.wora.hadoop;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.hadoop.io.Text;

import com.wora.constant.IOConstant;
import com.wora.util.ParseLineBean;

public class Test implements IOConstant {
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyy hh:mm:ss", Locale.ENGLISH);

	public static void main(String[] args) throws Exception {

		// String fileName = "LogMonitor.properties";
		//
		// File file = new File(fileName);
		// Properties properties = new Properties();
		// properties.load(new FileInputStream(file));
		//
		// Tomcat7 tomcat7 = new Tomcat7();
		// tomcat7.initialize(properties);
		// tomcat7.startTomcat();

		String text = "02 Jan 2016 06:36:21getChangeDealerEmployee4";
		ParseLineBean.parseMethod(new Text(text));

	}
}
