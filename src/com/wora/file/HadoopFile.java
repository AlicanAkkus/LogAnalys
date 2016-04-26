package com.wora.file;

public class HadoopFile extends AbstractFile {
	String id;
	String fileName;
	String hdfsLocation;
	String name;
	String source;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
		return "HadoopFile [id=" + id + ", fileName=" + fileName + ", hdfsLocation=" + hdfsLocation + ", name=" + name + ", source=" + source + "]";
	}
	
}
