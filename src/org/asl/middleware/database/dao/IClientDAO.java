package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.common.timer.middleware.MiddlewareTimer;
import org.asl.middleware.database.model.Message;

public interface IClientDAO {
	
	public int registerClient(MiddlewareTimer timer, int requestId) throws HandshakeException;
	public Message readMessageFromSender(int sender, int receiver, MiddlewareTimer timer, int requestId) throws ReadMessageFromSenderException;
	public List<Integer> getRegisteredClients(MiddlewareTimer timer, int requestId) throws GetRegisteredClientsException;
}
