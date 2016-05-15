package com.wora.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wora.bean.LineBean;

public class TemplateValidationProcessor {

	private static Logger logger = Logger.getLogger(TemplateValidationProcessor.class);

	public static boolean validdation(String value, LineBean bean) throws Exception {

		switch (bean.getType()) {
		case "date":
			return validateDate(value, bean.getPattern(), bean.getFormat());
		case "string":
			return validateString(value, bean.getPattern());
		case "integer":
			return validateNumber(value, bean.getPattern());
		default:
			break;
		}
		return true;
	}

	public static boolean validateDate(String value, String pattern, String format) throws Exception {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);

			if (StringUtils.isBlank(value)) {
				return false;
			}

			// less than %05-11-2016%"
			if (pattern.contains("less than") || pattern.contains("greater than") || pattern.contains("lt") || pattern.contains("gt")) {
				String compareDateStr = pattern.substring(pattern.indexOf("%") + 1, pattern.lastIndexOf("%"));
				Date compareDate = formatter.parse(compareDateStr);
				Date valueDate = formatter.parse(value);

				logger.debug("Compare two date : " + compareDateStr + " and " + valueDate);
				
				if (pattern.contains("less than") || pattern.contains("lt")) {
					return valueDate.before(compareDate);
				} else if (pattern.contains("greater than") || pattern.contains("gt")) {
					return valueDate.after(compareDate);
				} else {
					return true;
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		}

		return true;
	}

	public static boolean validateString(String value, String pattern) throws Exception {

		try {

			if (StringUtils.isBlank(value)) {
				return false;
			}

			// less than %05-11-2016%"
			if (pattern.contains("equals") || pattern.contains("like")) {
				String compareStr = pattern.substring(pattern.indexOf("%") + 1, pattern.lastIndexOf("%"));

				if (pattern.contains("equals")) {
					return value.equalsIgnoreCase(compareStr);
				} else if (pattern.contains("like")) {
					return value.contains(compareStr);
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		}

		return true;
	}
	
	public static boolean validateNumber(String value, String pattern) throws Exception {

		try {

			if (StringUtils.isBlank(value)) {
				return false;
			}


			// less than %05-11-2016%"
			if (pattern.contains("less than") || pattern.contains("greater than") || pattern.contains("lt") || pattern.contains("gt")) {
				String compareNumber = pattern.substring(pattern.indexOf("%") + 1, pattern.lastIndexOf("%"));

				if (pattern.contains("less than") || pattern.contains("lt")) {
					return Integer.valueOf(value) < Integer.valueOf(compareNumber);
				} else if (pattern.contains("greater than") || pattern.contains("gt")) {
					return Integer.valueOf(value) > Integer.valueOf(compareNumber);
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		}

		return true;
	}
}
