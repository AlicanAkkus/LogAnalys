package com.wora.bean;

import java.util.Date;

import com.wora.enums.LineEnum;

public class MethodLineBean extends LineBean implements Comparable {

	String methodName;
	int executeTime;
	Date date;

	public MethodLineBean() {

	}

	public MethodLineBean(LineEnum lineType, String methodName, int executiTime, Date date) {
		super(lineType);
		this.methodName = methodName;
		executeTime = executiTime;
		this.date = date;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(int executeTime) {
		this.executeTime = executeTime;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "MethodLineBean [methodName=" + methodName + ", executeTime=" + executeTime + ", date=" + dateFormat.format(date) + ", LineType=" + getType()
				+ ", AnalysType=" + getAnalysType() + "]";
	}

	@Override
	public int compareTo(Object object) {
		MethodLineBean methodLineBean = (MethodLineBean) object;
		if (executeTime == methodLineBean.getExecuteTime()) {
			return 0;
		} else if (executeTime > methodLineBean.getExecuteTime()) {
			return 1;
		} else {
			return -1;
		}
	}
}
