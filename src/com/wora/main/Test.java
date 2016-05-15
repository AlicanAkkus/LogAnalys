package com.wora.main;

import org.w3c.dom.Document;

import com.wora.constant.IOConstant;
import com.wora.template.HadoopTemplate;
import com.wora.util.XmlUtils;

public class Test implements IOConstant {
	public static void main(String[] args) throws Exception {
		
		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");
//		
//		Date date1 = dateFormat.parse("05-11-2016");
//		Date date2 = dateFormat.parse("05-12-2016");
//		System.out.println("Date1 : " + date1);
//		System.out.println("Date2 : " + date2);
//		
//		if(date1.before(date2)){
//			System.out.println("beforee");
//		}else{
//			System.out.println("agteer");
//		}
		
		Analys analys = Analys.getInstance();
		Document confXml = XmlUtils.loadXmlFromFile("LogAnalys.xml");
		analys.initializeServices(confXml);
		
		StringBuilder data = new StringBuilder();
		data.append(STX).append(SUB).append("05-11-2016").append(SUB).append("contextInitialized").append(SUB).append("starting server");
		data.append(SUB).append("175").append(SUB).append(ETX);
	//	String data = "<STX><SUB>05-11-2016<SUB>contextInitialized<SUB>starting server<SUB>175<SUB><ETX>";

		for(HadoopTemplate hadoopTemplate : analys.getInstance().getTemplatesPool().values()){
			hadoopTemplate.test(data.toString());
		}
		
		
		
		
		
	}
}
