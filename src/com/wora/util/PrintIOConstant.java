package com.wora.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.apache.log4j.Logger;

import com.wora.constant.IOConstant;

public class PrintIOConstant implements IOConstant {

	static Logger logger = Logger.getLogger(PrintIOConstant.class);

	public static void main(String[] args) {

		String str = "getChangeDealerEmployee920000009";
		String str2 = "getChangeDealerEmployee9";
		System.out.println(convertLogMessageToString(str));
		System.out.println(convertLogMessageToString(str2));

	}

	public static String convertLogMessageToString(String logMessage) {

		StringBuffer s = new StringBuffer();
		CharacterIterator it = new StringCharacterIterator(logMessage);

		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			switch (ch) {
			case IOConstant.START_OF_TEXT:
				s.append("<STX>");
				break;
			case IOConstant.END_OF_TEXT:
				s.append("<ETX>");
				break;
			case IOConstant.METHOD_DECLARATION:
				s.append("<NAK>");
				break;
			case IOConstant.VARIABLE_DECLARATION:
				s.append("<ETB>");
				break;
			case IOConstant.LOOP_DECLARATION:
				s.append("<CAN>");
				break;
			case IOConstant.INITALIZE_DECLARATION:
				s.append("<SUB>");
				break;
			case IOConstant.DESTROY_DECLARATION:
				s.append("<ESC>");
				break;
			default:
				s.append(ch);
				break;
			}
		}
		System.out.println("(Log Msg) : " + s.toString());
		logger.debug("(Log Msg) : " + s.toString());
		return s.toString();
	}

}
