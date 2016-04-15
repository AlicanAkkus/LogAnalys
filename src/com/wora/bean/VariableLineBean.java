package com.wora.bean;

import java.util.Date;

import com.wora.enums.LineEnum;

public class VariableLineBean extends LineBean implements Comparable {

	String methodName;
	int sumMemory;
	Date date;

	public VariableLineBean() {

	}

	public VariableLineBean(LineEnum lineType, String methodName, int sumMemory, Date date) {
		super(lineType);
		this.methodName = methodName;
		this.sumMemory = sumMemory;
		this.date = date;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getSumMemory() {
		return sumMemory;
	}

	public void setSumMemory(int sumMemory) {
		this.sumMemory = sumMemory;
	}

	@Override
	public String toString() {
		return "VariableLineBean [methodName=" + methodName + ", sumMemory=" + sumMemory + ", date=" + dateFormat.format(date) + ", LineType=" + getType()
				+ ", AnalysType=" + getAnalysType() + "]";
	}

	@Override
	public int compareTo(Object object) {
		VariableLineBean variableLineBean = (VariableLineBean) object;

		if (sumMemory == variableLineBean.getSumMemory()) {
			return 0;
		} else if (sumMemory > variableLineBean.getSumMemory()) {
			return 1;
		} else {
			return -1;
		}

	}

}
