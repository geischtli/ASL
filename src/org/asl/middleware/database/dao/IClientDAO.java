package org.asl.middleware.database.dao;

import java.util.List;

import org.asl.common.request.types.exceptions.GetRegisteredClientsException;
import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.common.timing.ASLTimer;
import org.asl.middleware.database.model.Message;

public interface IClientDAO {
	
	public int registerClient(ASLTimer timer, int requestId) throws HandshakeException;
	public Message readMessageFromSender(int sender, int receiver, ASLTimer timer, int requestId) throws ReadMessageFromSenderException;
	public List<Integer> getRegisteredClients(ASLTimer timer, int requestId) throws GetRegisteredClientsException;
}
