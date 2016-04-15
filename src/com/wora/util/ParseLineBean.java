package com.wora.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;

import com.wora.bean.ContextDestroyLineBean;
import com.wora.bean.ContextInitializeLineBean;
import com.wora.bean.LineBean;
import com.wora.bean.MethodLineBean;
import com.wora.bean.VariableLineBean;
import com.wora.constant.IOConstant;
import com.wora.enums.LineEnum;

public class ParseLineBean implements IOConstant {

	static Logger logger = Logger.getLogger(ParseLineBean.class);
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH);

	public static LineBean parse(Text text, LineEnum lineType) throws Exception {

		logger.debug("Parse line is started.");

		if (lineType == LineEnum.METHOD_DECLARATION) {
			return parseMethod(text);
		} else if (lineType == LineEnum.VARIABLE_DECLARATION) {
			return parseVariable(text);
		} else if (lineType == LineEnum.INITALIZE_DECLARATION) {
			return parseInitalize(text);
		} else if (lineType == LineEnum.DESTROY_DECLARATION) {
			return parseDestroy(text);
		} else {
			throw new RuntimeException("Unknow Parse Type!");
		}

	}

	public static LineBean parseMethod(Text text) throws Exception {
		logger.debug("Line type is method.");
		System.out.println("Line type is method.");
		// 02 Jan 2016 06:36:21getChangeDealerEmployee4
		String lineText = text.toString();
		int startOfText = lineText.indexOf(START_OF_TEXT);
		int endOfText = lineText.indexOf(END_OF_TEXT);

		if (startOfText != -1 && endOfText != -1) {
			MethodLineBean methodLineBean = new MethodLineBean();

			lineText = lineText.substring(startOfText + 1, endOfText);
			System.out.println(lineText);
			String[] seqeunces = lineText.split(String.valueOf(METHOD_DECLARATION));

			Date date = dateFormat.parse(seqeunces[1]);
			String methodName = seqeunces[2];
			int executeTime = Integer.valueOf(seqeunces[3]);

			methodLineBean.setType(LineEnum.METHOD_DECLARATION);
			methodLineBean.setDate(date);
			methodLineBean.setMethodName(methodName);
			methodLineBean.setExecuteTime(executeTime);

			System.out.println(methodLineBean.toString());
			return methodLineBean;
		}

		return null;
	}

	public static LineBean parseVariable(Text text) throws Exception {
		logger.debug("Line type is variable.");
		System.out.println("Line type is variable.");
		// 02 Jan 2016 06:36:21createDealerEmployee212
		String lineText = text.toString();
		int startOfText = lineText.indexOf(START_OF_TEXT);
		int endOfText = lineText.indexOf(END_OF_TEXT);

		if (startOfText != -1 && endOfText != -1) {
			VariableLineBean variableLineBean = new VariableLineBean();
			lineText = lineText.substring(startOfText + 1, endOfText);
			System.out.println(lineText);
			String[] seqeunces = lineText.split(String.valueOf(VARIABLE_DECLARATION));

			Date date = dateFormat.parse(seqeunces[1]);
			String methodName = seqeunces[2];
			int sum = Integer.valueOf(seqeunces[3]);

			variableLineBean.setType(LineEnum.VARIABLE_DECLARATION);
			variableLineBean.setDate(date);
			variableLineBean.setMethodName(methodName);
			variableLineBean.setSumMemory(sum);

			System.out.println(variableLineBean);
			return variableLineBean;
		}

		return null;
	}

	public static LineBean parseInitalize(Text text) throws Exception {
		logger.debug("Line type is initialize.");
		System.out.println("Line type is initialize.");
		// contextInitializedLog4j and Scheduler job initializing.168
		String lineText = text.toString();
		int startOfText = lineText.indexOf(START_OF_TEXT);
		int endOfText = lineText.indexOf(END_OF_TEXT);

		if (startOfText != -1 && endOfText != -1) {
			ContextInitializeLineBean contextInitializeLineBean = new ContextInitializeLineBean();
			lineText = lineText.substring(startOfText + 1, endOfText);
			System.out.println("Substring Text : " + lineText);
			String[] seqeunces = lineText.split(String.valueOf(INITALIZE_DECLARATION));

			Date date = dateFormat.parse(seqeunces[1]);
			String methodName = seqeunces[2];
			String description = seqeunces[3];
			int executeTime = Integer.valueOf(seqeunces[4]);

			contextInitializeLineBean.setContextInializeMethod(methodName);
			contextInitializeLineBean.setType(LineEnum.INITALIZE_DECLARATION);
			contextInitializeLineBean.setDescription(description);
			contextInitializeLineBean.setExecuteTime(executeTime);
			contextInitializeLineBean.setDate(date);

			System.out.println(contextInitializeLineBean.toString());
			return contextInitializeLineBean;
		}

		return null;
	}

	public static LineBean parseDestroy(Text text) throws Exception {
		logger.debug("Line type is destroy.");
		System.out.println("Line type is destroy.");
		// 02 Jan 2016 06:36:26contextDestroyedCleaning log4j and scheduler job23

		String lineText = text.toString();
		int startOfText = lineText.indexOf(START_OF_TEXT);
		int endOfText = lineText.indexOf(END_OF_TEXT);

		if (startOfText != -1 && endOfText != -1) {
			ContextDestroyLineBean contextDestroyLineBean = new ContextDestroyLineBean();
			lineText = lineText.substring(startOfText + 1, endOfText);
			System.out.println("Substring Text : " + lineText);
			String[] seqeunces = lineText.split(String.valueOf(DESTROY_DECLARATION));

			Date date = dateFormat.parse(seqeunces[1]);
			String methodName = seqeunces[2];
			String description = seqeunces[3];
			int executeTime = Integer.valueOf(seqeunces[4]);

			contextDestroyLineBean.setContexyDestroyedMethod(methodName);
			contextDestroyLineBean.setType(LineEnum.DESTROY_DECLARATION);
			contextDestroyLineBean.setDescription(description);
			contextDestroyLineBean.setExecuteTime(executeTime);
			contextDestroyLineBean.setDate(date);

			System.out.println(contextDestroyLineBean.toString());
			return contextDestroyLineBean;
		}

		return null;
	}

}
