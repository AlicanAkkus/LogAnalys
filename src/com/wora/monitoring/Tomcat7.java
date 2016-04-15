package com.wora.monitoring;

import java.io.File;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.catalina.realm.RealmBase;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.IntrospectionUtils;

public class Tomcat7 {
	protected static Logger logger = Logger.getLogger(Tomcat7.class);
	private String catalinaHome = "";
	private Tomcat tomcat = null;
	private Host host = null;
	private int monitorPort = 8080;
	private boolean useHttps = false;
	private String httpsKeyPass;
	private String httpsKeyStore;
	private String[] ctxPath = new String[10];
	private String[] ctxName = new String[10];

	private RealmBase realmBase = null;

	public Tomcat7() {

	}

	/**
	 * This method Starts the Tomcat server.
	 */
	public void startTomcat() throws Exception {

		// Create an embedded server
		tomcat = new Tomcat();
		tomcat.setBaseDir(catalinaHome);

		Connector httpConnector = tomcat.getConnector();
		httpConnector.setPort(monitorPort);

		if (useHttps) {
			httpConnector.setScheme("https");
			httpConnector.setSecure(true);
			httpConnector.setProtocol("SSL");
			httpConnector.setAttribute("SSLEnabled", true);
			// httpConnector.setAttribute("keyAlias", keyAlias);
			// httpsConnector.setAttribute("keystorePass", password);
			// httpsConnector.setAttribute("keystoreFile", keystorePath);
			// httpsConnector.setAttribute("clientAuth", "false");

			// Https settings
			IntrospectionUtils.setProperty(httpConnector, "sslProtocol", "TLS");
			IntrospectionUtils.setProperty(httpConnector, "keypass", httpsKeyPass);
			IntrospectionUtils.setProperty(httpConnector, "keystore", httpsKeyStore);
			// IntrospectionUtils.setProperty(httpConnector, "port", "" + monitorPort);

			// Supported ciphers. we eliminate weak cipher and tested via
			// SSLDigger
			StringBuilder supportedCiphers = new StringBuilder();
			supportedCiphers.append("SSL_RSA_WITH_RC4_128_MD5, SSL_RSA_WITH_RC4_128_SHA, TLS_RSA_WITH_AES_128_CBC_SHA,");
			supportedCiphers.append("TLS_DHE_RSA_WITH_AES_128_CBC_SHA, TLS_DHE_DSS_WITH_AES_128_CBC_SHA,");
			supportedCiphers.append("SSL_RSA_WITH_3DES_EDE_CBC_SHA, SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA, SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA");

			IntrospectionUtils.setProperty(httpConnector, "ciphers", supportedCiphers.toString());
		}

		InetAddress address = InetAddress.getLocalHost();
		if (address != null) {
			IntrospectionUtils.setProperty(httpConnector, "address", "" + address);
		}
		httpConnector.setEnableLookups(false);

		tomcat.getHost().setAppBase(catalinaHome);
		tomcat.getHost().setAutoDeploy(true);
		tomcat.enableNaming();

		StringBuilder sb = new StringBuilder("\n\nStarting embedded tomcat. Monitoring URLs :");

		int length = 0;
		while (ctxName[length] != null) {
			String contextPath = ctxPath[length];
			String contextName = ctxName[length];
			Context context = tomcat.addWebapp(tomcat.getHost(), contextName, contextPath);
			context.setRealm(realmBase);
			context.setReloadable(true);
			//
			sb.append("\n" + httpConnector.getScheme() + "://" + address.getHostName() + ":" + monitorPort + contextName + "\n");
			length++;
		}

		logger.info(sb.toString());

		// Start the embedded server
		tomcat.start();
		// tomcat.getServer().await();

	}

	/**
	 * This method Stops the Tomcat server.
	 */
	public void stopTomcat() throws Exception {
		logger.info("Stopping Tomcat");
		// Stop the embedded server
		tomcat.stop();
	}

	public void initialize(Properties appProps) throws Exception {

		catalinaHome = appProps.getProperty("monitor.web.root", "web");
		int contextCounter = 1;
		String context = appProps.getProperty("monitor.web.context." + contextCounter + ".path");

		while (org.apache.commons.lang.StringUtils.isNotBlank(context)) {
			// Birden fazla contextolabilir.
			ctxPath[contextCounter - 1] = appProps.getProperty("monitor.web.context." + contextCounter + ".path", "TOFIntegratorV2Monitor/WebContent");
			ctxName[contextCounter - 1] = appProps.getProperty("monitor.web.context." + contextCounter + ".name", "/tofmonitor");
			contextCounter++;
			context = appProps.getProperty("monitor.web.context." + contextCounter + ".path");
		}

		monitorPort = Integer.parseInt(appProps.getProperty("monitor.port", "8080").trim());
		httpsKeyStore = appProps.getProperty("monitor.https.keystore");
		useHttps = appProps.getProperty("monitor.use.https", "false").equalsIgnoreCase("true");
		String realmDigest = appProps.getProperty("monitor.realm.digest", "").trim();

			// // set the memory realm
			realmBase = new MemoryRealm();

		if (org.apache.commons.lang.StringUtils.isNotBlank(realmDigest)) {
			realmBase.setDigest(realmDigest.toUpperCase());
		}

	}

	public String getCatalinaHome() {
		return catalinaHome;
	}

	public void setCatalinaHome(String catalinaHome) {
		this.catalinaHome = catalinaHome;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public int getMonitorPort() {
		return monitorPort;
	}

	public void setMonitorPort(int monitorPort) {
		this.monitorPort = monitorPort;
	}

	public boolean isUseHttps() {
		return useHttps;
	}

	public void setUseHttps(boolean useHttps) {
		this.useHttps = useHttps;
	}

	public String getHttpsKeyPass() {
		return httpsKeyPass;
	}

	public void setHttpsKeyPass(String httpsKeyPass) {
		this.httpsKeyPass = httpsKeyPass;
	}

	public String getHttpsKeyStore() {
		return httpsKeyStore;
	}

	public void setHttpsKeyStore(String httpsKeyStore) {
		this.httpsKeyStore = httpsKeyStore;
	}

	public RealmBase getRealmBase() {
		return realmBase;
	}

	public void setRealmBase(RealmBase realmBase) {
		this.realmBase = realmBase;
	}

}