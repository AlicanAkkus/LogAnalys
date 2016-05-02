package com.wora.socket.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class SocketHandler extends IoHandlerAdapter {

	Logger logger = Logger.getLogger(SocketHandler.class);

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.error(cause.getMessage());
	}

	@Override
	public void messageSent(IoSession ioSession, Object obj) throws Exception {
		logger.info("\nSent message :" + obj.toString());
	}

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		String message = obj.toString();
		logger.info("\nNew Message Received:" + message);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		logger.info("\nIDLE " + session.getIdleCount(status));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		logger.info("\nSession closed.  Session ID:" + session.getId() + " - Local address:" + session.getLocalAddress() + " - Remote Address:"
				+ session.getRemoteAddress() + " - Service Address:" + session.getServiceAddress() + " - TransportMetadata:" + session.getTransportMetadata());

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		logger.info("\nSession created .. ");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		logger.info("\nSession opened. Session ID:" + session.getId() + " - Local address:" + session.getLocalAddress() + " - Remote Address:"
				+ session.getRemoteAddress() + " - Service Address:" + session.getServiceAddress() + " - TransportMetadata:" + session.getTransportMetadata());

	}

}
