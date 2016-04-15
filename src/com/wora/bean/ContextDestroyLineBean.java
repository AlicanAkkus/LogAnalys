package com.wora.bean;

import java.util.Date;

import com.wora.enums.LineEnum;

public class ContextDestroyLineBean extends LineBean implements Comparable {

	String contexyDestroyedMethod;
	String description;
	int executeTime;
	Date date;

	public ContextDestroyLineBean() {

	}

	public ContextDestroyLineBean(LineEnum lineType, String methodName, String description, int executeTime, Date date) {
		super(lineType);
		contexyDestroyedMethod = methodName;
		this.description = description;
		this.executeTime = executeTime;
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContexyDestroyedMethod() {
		return contexyDestroyedMethod;
	}

	public void setContexyDestroyedMethod(String contexyDestroyedMethod) {
		this.contexyDestroyedMethod = contexyDestroyedMethod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(int executeTime) {
		this.executeTime = executeTime;
	}

	@Override
	public String toString() {
		return "ContextDestroyLineBean [contexyDestroyedMethod=" + contexyDestroyedMethod + ", description=" + description + ", executeTime=" + executeTime
				+ ", date=" + dateFormat.format(date) + ", LineType=" + getType() + ", AnalysType=" + getAnalysType() + "]";
	}

	@Override
	public int compareTo(Object object) {
		ContextDestroyLineBean contextDestroyLineBean = (ContextDestroyLineBean) object;
		if (executeTime == contextDestroyLineBean.getExecuteTime()) {
			return 0;
		} else if (executeTime > contextDestroyLineBean.getExecuteTime()) {
			return 1;
		} else {
			return -1;
		}
	}

}
