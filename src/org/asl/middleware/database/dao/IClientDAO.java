package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.common.timing.TimeLogger;
import org.asl.middleware.database.model.Message;

public interface IClientDAO {
	
	public int registerClient(int clientId, int requestId) throws HandshakeException;
	public Message readMessageFromSender(int sender, int receiver, int clientId, int requestId) throws ReadMessageFromSenderException;
	public List<Integer> getRegisteredClients(int clientId, int requestId) throws GetRegisteredClientsException;
}
