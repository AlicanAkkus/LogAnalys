package com.wora.bean;


public class LineBean {
	String name;
	String descpriton;
	int sequence;
	String format;
	String pattern;
	String type;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescpriton() {
		return descpriton;
	}
	public void setDescpriton(String descpriton) {
		this.descpriton = descpriton;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "LineBean [name=" + name + ", descpriton=" + descpriton + ", sequence=" + sequence + ", format=" + format + ", pattern=" + pattern + ", type="
				+ type + "]";
	}
	
}
