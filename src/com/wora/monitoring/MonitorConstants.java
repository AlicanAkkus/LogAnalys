package com.wora.monitoring;

import java.util.HashMap;

public class MonitorConstants {

	public static final String CONTEXT_MESSAGE_STATES = "context.message.states";
	public static final String CONTEXT_ENRICHMENT_STATUS = "context.ticket.status";
	public static final String CONTEXT_ADAPTOR_LOOKUPS = "context.adaptor.lookups";
	public static final String CONTEXT_TCID_LOOKUPS = "context.tcid.lookups";
	public static final String MONITOR_MESSAGES = "monitoring.messages";
	public static final String SESSION_MSG_LIST = "session.message.list";
	public static final String SESSION_FAILED_TICKET_LIST = "session.failed.ticket.list";
	public static final String SELECTED_FAILED_TICKET = "request.selected.failed.ticket";
	// Monitor
	public static String monitorVersion = "1.2";
	public static String monitorBuildDate = "24/12/2012";
	// Log Viewer
	public static boolean logViewerEnabled;
	public static boolean logViewerAutoScroll;
	public static boolean logViewerStartAtBegining;
	public static int logViewerFontSize;
	public static String logViewerVersion = "1.1";
	public static String logViewerBuildDate = "24/12/2012";

	// Channel Monitor
	public static boolean channelMonitorEnabled;
	public static boolean channelMonitorAutoScroll;
	public static int channelMonitorFontSize;
	public static String channelMonitorVersion = "1.1";
	public static String channelMonitorBuildDate = "24/12/2012";
	public static boolean channelMonitorSendMessageEnabled;
	public static String channelLogFile;

	public static HashMap<String, String> availableLogs = new HashMap<String, String>();

}
