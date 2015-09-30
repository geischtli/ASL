package org.asl.middleware.database.dao;

import org.asl.common.request.types.exceptions.HandshakeException;

public interface IClientDAO {
	
	public int registerClient() throws HandshakeException;
}
