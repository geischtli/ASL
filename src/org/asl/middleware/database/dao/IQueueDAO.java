package org.asl.middleware.database.dao;

import org.asl.common.message.types.exceptions.CreateQueueException;

public interface IQueueDAO {

	public int createQueue() throws CreateQueueException;
}
