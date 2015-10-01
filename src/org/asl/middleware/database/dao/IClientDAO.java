package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.HandshakeException;
import org.asl.common.request.types.exceptions.ReadMessageFromSenderException;
import org.asl.middleware.database.model.Message;

public interface IClientDAO {
	
	public int registerClient() throws HandshakeException;
	public Message readMessageFromSender(int sender, int receiver) throws ReadMessageFromSenderException;
}
