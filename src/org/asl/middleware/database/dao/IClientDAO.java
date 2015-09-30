package org.asl.middleware.database.dao;

import org.asl.common.message.types.exceptions.HandshakeException;

public interface IClientDAO {
	
	public int registerClient() throws HandshakeException;
}
