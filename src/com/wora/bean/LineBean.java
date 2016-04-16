package com.wora.bean;


public class LineBean {
	String name;
	String descpriton;
	int sequence;
	
	
	public LineBean(String name, String descpriton, int sequence) {
		super();
		this.name = name;
		this.descpriton = descpriton;
		this.sequence = sequence;
	}
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
	
	@Override
	public String toString() {
		return "LineBean [name=" + name + ", descpriton=" + descpriton + ", sequence=" + sequence + "]";
	}
	
}
