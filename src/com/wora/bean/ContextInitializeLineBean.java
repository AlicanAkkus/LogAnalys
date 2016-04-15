package com.wora.bean;

import java.util.Date;

import com.wora.enums.LineEnum;

public class ContextInitializeLineBean extends LineBean implements Comparable {

	String contextInializeMethod;
	String description;
	int executeTime;
	Date date;

	public ContextInitializeLineBean() {

	}

	public ContextInitializeLineBean(LineEnum lineType, String methodName, String description, int executeTime, Date date) {
		super(lineType);
		contextInializeMethod = methodName;
		this.description = description;
		this.executeTime = executeTime;
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContextInializeMethod() {
		return contextInializeMethod;
	}

	public void setContextInializeMethod(String contextInializeMethod) {
		this.contextInializeMethod = contextInializeMethod;
	}

	public int getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(int executeTime) {
		this.executeTime = executeTime;
	}

	@Override
	public String toString() {
		return "ContextInitializeLineBean [contextInializeMethod=" + contextInializeMethod + ", description=" + description + ", executeTime=" + executeTime
				+ ", date=" + dateFormat.format(date) + ", LineType=" + getType() + ", AnalysType=" + getAnalysType() + "]";
	}

	@Override
	public int compareTo(Object object) {
		ContextInitializeLineBean contextInitalizeLineBean = (ContextInitializeLineBean) object;
		if (executeTime == contextInitalizeLineBean.getExecuteTime()) {
			return 0;
		} else if (executeTime > contextInitalizeLineBean.getExecuteTime()) {
			return 1;
		} else {
			return -1;
		}
	}

}
