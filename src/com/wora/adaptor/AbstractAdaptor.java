package com.wora.adaptor;

import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

public abstract class AbstractAdaptor extends Thread{
	private Logger logger = Logger.getLogger(AbstractAdaptor.class);
	private boolean enabled = true;

	HashMap<String, String> customParams = new HashMap<>();
	Vector<Object> messageQueue = new Vector<>();
	
	public abstract void init(Element destination);
	public abstract void processMessage(Object message);
	
	@Override
	public void run() {
		logger.info("Adaptor is started , adaptor name : " + customParams.get("adaptorName"));
		
		while(enabled){
			if(messageQueue.size() > 0){
				Object message = messageQueue.get(0);
				processMessage(message);
			}
		}
		
		logger.info("Adaptor is finished , adaptor name : " + customParams.get("adaptorName"));
		
	}
	
	public void addQueue(Object message){
		this.messageQueue.add(message);
	}

	public HashMap<String, String> getCustomParams() {
		return customParams;
	}

	public void setCustomParams(HashMap<String, String> customParams) {
		this.customParams = customParams;
	}

}
