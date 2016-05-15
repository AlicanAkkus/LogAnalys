package com.wora.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

		ArrayList<String> MULTIPLE_AGREED_106 = new ArrayList<>();
		String multipleAgreed[] = ("MULTIPLE AGREED;MULTIPLE AGR").split(";");
		for (String s : multipleAgreed) {
			MULTIPLE_AGREED_106.add(s);
		}

		try {

			for (String keyword : MULTIPLE_AGREED_106) {
				String cData = "MULTIPLE AGREED 5151\n";

				int index = cData.indexOf(keyword);
				if (index > -1) {
					String value = cData.substring(index + keyword.length() + 1, cData.indexOf("\n", index + 1));
					System.out.println(value.replace(Character.toString((char) 13), ""));
				}
			}

		} catch (Exception e) {
		}

	}
}
