package com.wora.enums;

public enum LineEnum {
	
	
	METHOD_DECLARATION(1, "METHOD"), VARIABLE_DECLARATION(2,"VARIABLE"), INITALIZE_DECLARATION(3, "INITALIZE"), DESTROY_DECLARATION(4, "DESTROY");
	
	private String value;
	private int key;
	
	private LineEnum(int key, String value){
		this.key = key;
		this.value = value;
	}
	
	public static LineEnum getLineEnum(int key) throws Exception {
		switch (key) {
		case 1:
			return METHOD_DECLARATION;
		case 2:
			return VARIABLE_DECLARATION;
		case 3:
			return INITALIZE_DECLARATION;
		case 4:
			return DESTROY_DECLARATION;
		default:
			throw new Exception("Unknown enum key : " + key);
		}
	}
	
	public String getValue() {
		return value;
	}
	public int getKey() {
		return key;
	}
	
}
