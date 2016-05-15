package com.wora.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.apache.log4j.Logger;

import com.jcraft.jsch.IO;
import com.wora.constant.IOConstant;

public class IOUtils implements IOConstant {

	private static Logger logger = Logger.getLogger(IOUtils.class);

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
			case IOConstant.SOH:
				s.append("<SOH>");
				break;
			case IOConstant.STX:
				s.append("<STX>");
				break;
			case IOConstant.ETX:
				s.append("<ETX>");
				break;
			case IOConstant.EOT:
				s.append("<EOT>");
				break;
			case IOConstant.ENQ:
				s.append("<ENQ>");
				break;
			case IOConstant.ACK:
				s.append("<ACK>");
				break;
			case IOConstant.BEL:
				s.append("<BEL>");
				break;
			case IOConstant.BS:
				s.append("<BS>");
				break;
			case IOConstant.TAB:
				s.append("<TAB>");
				break;
			case IOConstant.LF:
				s.append("<LF>");
				break;
			case IOConstant.VT:
				s.append("<VT>");
				break;
			case IOConstant.FF:
				s.append("<FF>");
				break;
			case IOConstant.CR:
				s.append("<CR>");
				break;
			case IOConstant.SO:
				s.append("<SO>");
				break;
			case IOConstant.SI:
				s.append("<VT>");
				break;
			case IOConstant.DLE:
				s.append("<FF>");
				break;
			case IOConstant.DC1:
				s.append("<DC1>");
				break;
			case IOConstant.DC2:
				s.append("<DC2>");
				break;
			case IOConstant.DC3:
				s.append("<DC3>");
				break;
			case IOConstant.DC4:
				s.append("<DC4>");
				break;
			case IOConstant.EM:
				s.append("<EM>");
				break;
			case IOConstant.SUB:
				s.append("<SUB>");
				break;
			case IOConstant.ES:
				s.append("<ES>");
				break;
			case IOConstant.CAN:
				s.append("<CAN>");
				break;
			case IOConstant.NAK:
				s.append("<NAK>");
				break;
			case IOConstant.SYN:
				s.append("<SYN>");
				break;
			case IOConstant.ETB:
				s.append("<ETB>");
				break;
			case IOConstant.FS:
				s.append("<FS>");
				break;
			case IOConstant.GS:
				s.append("<GS>");
				break;
			case IOConstant.US:
				s.append("<US>");
				break;
			case IOConstant.RS:
				s.append("<RS>");
				break;
			default:
				s.append(ch);
				break;
			}
		}
		logger.debug("(Log Msg) : " + s.toString());
		return s.toString();
	}
	
	public static char getAsciiByValue(String value){
		switch((int)Integer.valueOf(value)) {
		case IOConstant.SOH:
			return IOConstant.SOH;
		case IOConstant.STX:
			return IOConstant.STX;
		case IOConstant.ETX:
			return IOConstant.ETX;
		case IOConstant.EOT:
			return IOConstant.EOT;
		case IOConstant.ENQ:
			return IOConstant.ENQ;
		case IOConstant.ACK:
			return IOConstant.ACK;
		case IOConstant.BEL:
			return IOConstant.BEL;
		case IOConstant.BS:
			return IOConstant.BS;
		case IOConstant.TAB:
			return IOConstant.TAB;
		case IOConstant.LF:
			return IOConstant.LF;
		case IOConstant.VT:
			return IOConstant.VT;
		case IOConstant.FF:
			return IOConstant.FF;
		case IOConstant.CR:
			return IOConstant.CR;
		case IOConstant.SO:
			return IOConstant.SO;
		case IOConstant.SI:
			return IOConstant.SI;
		case IOConstant.DLE:
			return IOConstant.DLE;
		case IOConstant.DC1:
			return IOConstant.DC1;
		case IOConstant.DC2:
			return IOConstant.DC2;
		case IOConstant.DC3:
			return IOConstant.DC3;
		case IOConstant.DC4:
			return IOConstant.DC4;
		case IOConstant.EM:
			return IOConstant.EM;
		case IOConstant.SUB:
			return IOConstant.SUB;
		case IOConstant.ES:
			return IOConstant.ES;
		case IOConstant.CAN:
			return IOConstant.CAN;
		case IOConstant.NAK:
			return IOConstant.NAK;
		case IOConstant.SYN:
			return IOConstant.SYN;
		case IOConstant.ETB:
			return IOConstant.ETB;
		case IOConstant.FS:
			return IOConstant.FS;
		case IOConstant.GS:
			return IOConstant.GS;
		case IOConstant.US:
			return IOConstant.US;
		case IOConstant.RS:
			return IOConstant.RS;
		}
		return 0;
	}

}
