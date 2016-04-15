package com.wora.test;


public class Application {

	private final String name = "Log Analys ";
	private final String version = "1.0";
	private final String buildDate = "07/12/2015 - 09:15";

	public String getNameAndVersion() {
		return name + " v" + version;
	}

	public String getFullDescription() {
		return name + " - Version : " + version + " - Build Date : " + buildDate;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getBuildDate() {
		return buildDate;
	}
}
