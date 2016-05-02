package com.wora.adaptor;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wora.socket.handler.SocketHandler;

public class SocketAdaptor extends AbstractAdaptor {
	Logger logger = Logger.getLogger(SocketAdaptor.class);
	NioSocketConnector socketConnector = null;
	IoSession socketSession = null;
	String SERVER_ADRESS = null;
	int SERVER_PORT = 5003;
	int DELAY = 1000;
	
	@Override
	public void init(Element destination) {

		try {
			socketConnector = new NioSocketConnector();

			socketConnector.getFilterChain().addLast("logger", new LoggingFilter());
			socketConnector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
			socketConnector.setHandler(new SocketHandler());

			NodeList params = XPathAPI.selectNodeList(destination, "param");
			for (int i = 0; i < params.getLength(); i++) {
				Element param = (Element) params.item(i);

				String name = param.getAttribute("name");
				String value = param.getAttribute("value");
				if ("bufferSize".equalsIgnoreCase(name)) {
					socketConnector.getSessionConfig().setReadBufferSize(Integer.valueOf(value));
				} else if ("idleTime".equalsIgnoreCase(name)) {
					socketConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, Integer.valueOf(value));
				}else if("serverAdress".equalsIgnoreCase(name)){
					SERVER_ADRESS = value;
				}else if("serverPort".equalsIgnoreCase(name)){
					SERVER_PORT = Integer.valueOf(value);
				}else if("delay".equalsIgnoreCase(name)){
					DELAY = Integer.valueOf(value);
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		}

	}

	@Override
	public void processMessage(Object message) {
		logger.info("Process message is started..");
		
		try{

			Thread.sleep(DELAY);
			sendMessage(message.toString());
			
		}catch(Exception e){
			logger.error(e, e);
		}
	}

	public void closeServerConnection() {

		// senkron kapat
		if (socketSession != null) {
			socketSession.close(false);
		}
		if (socketConnector != null) {
			socketConnector.dispose();
		}

	}

	public void openServerConnection() {

		try {
			// session baglı degilse once bir connect olalım sonrasında session alalım.
			if (socketSession == null || socketSession != null && !socketSession.isConnected()) {
				ConnectFuture future = socketConnector.connect(new InetSocketAddress(SERVER_ADRESS, SERVER_PORT));
				future.awaitUninterruptibly();
				socketSession = future.getSession();
			} else {
				logger.info("Session already active.");
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	public void sendMessage(String message) {
		// server alive degilse once acmayi deneyelim
		if (!isServerConnectionAlive()) {
			openServerConnection();
		}
		// server alive , o zaman sessiona yazalim mesajimizi.
		if (isServerConnectionAlive()) {
			logger.info("session is open, sending message : " + message);
			socketSession.write(message);
		}
	}
	
	public boolean isServerConnectionAlive() {
		return socketSession != null && socketSession.isConnected();
	}

}
