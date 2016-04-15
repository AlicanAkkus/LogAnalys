package com.wora.bean;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.wora.enums.LineEnum;

public abstract class LineBean {
	private LineEnum type;
	private String analysType = "DEFAULT";
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.ENGLISH);

	public LineBean() {
	}

	public LineBean(LineEnum type) {
		this.type = type;
	}

	public LineEnum getType() {
		return type;
	}

	public void setType(LineEnum type) {
		this.type = type;
	}

	public String getAnalysType() {
		return analysType;
	}

	public void setAnalysType(String analysType) {
		this.analysType = analysType;
	}

}
