package com.wora.template;

import java.util.HashMap;
import java.util.LinkedList;

import com.wora.adaptor.AbstractAdaptor;
import com.wora.bean.LineBean;
import com.wora.file.HadoopFile;

/**
 * @author wora
 * @version 2.0
 * */
public class HadoopTemplate {
	String id;
	String templateName;
	HashMap<String, String> params;
	HadoopFile hadoopFile;
	LinkedList<LineBean> dataLine;
	LinkedList<AbstractAdaptor> adaptors;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public LinkedList<LineBean> getDataLine() {
		return dataLine;
	}

	public void setDataLine(LinkedList<LineBean> dataLine) {
		this.dataLine = dataLine;
	}

	public HadoopFile getHadoopFile() {
		return hadoopFile;
	}

	public void setHadoopFile(HadoopFile hadoopFile) {
		this.hadoopFile = hadoopFile;
	}
	
	public LinkedList<AbstractAdaptor> getAdaptors() {
		return adaptors;
	}

	public void setAdaptors(LinkedList<AbstractAdaptor> adaptors) {
		this.adaptors = adaptors;
	}

	@Override
	public String toString() {
		return "HadoopTemplate [id=" + id + ", templateName=" + templateName + ", params=" + params + ", dataLine=" + dataLine + ", hadoopFile="
				+ hadoopFile.toString() + "]";
	}

}
