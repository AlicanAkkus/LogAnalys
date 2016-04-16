package com.wora.file;

public class HadoopFile extends AbstractFile {
	String fileName;
	String hdfsLocation;
	String name;
	String source;
	
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getHdfsLocation() {
		return hdfsLocation;
	}
	public void setHdfsLocation(String hdfsLocation) {
		this.hdfsLocation = hdfsLocation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Override
	public String toString() {
		return "HadoopFile [fileName=" + fileName + ", hdfsLocation=" + hdfsLocation + ", name=" + name + ", source=" + source + "]";
	}
	
}
